package com.boundless.mixin;

import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class AttackMixin {
    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    public void boundless$attack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        if (!HeroUtils.isHero(player)) return;
        if (HeroUtils.combatModeEnabled(player)) ci.cancel();
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isBlockBreakingRestricted")
    public boolean boundless$isBlockBreakingRestricted(boolean original) {
        return original || HeroUtils.isHero((PlayerEntity) (Object)this) && HeroUtils.combatModeEnabled((PlayerEntity) (Object)this);
    }
}
