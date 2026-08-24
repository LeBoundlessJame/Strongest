package com.boundless.mixin;

import com.boundless.hero.api.HeroArmor;
import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class AttackMixin {
    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    public void boundless$attack(Entity target, CallbackInfo ci) {
        if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HeroArmor) return;
        if (HeroUtils.combatModeEnabled((PlayerEntity) (Object)this)) ci.cancel();
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isBlockBreakingRestricted")
    public boolean boundless$isBlockBreakingRestricted(boolean original) {
        if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HeroArmor) return original;
        return original || (HeroUtils.combatModeEnabled((PlayerEntity) (Object)this));
    }
}
