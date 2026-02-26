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
        if (HeroUtils.combatModeEnabled((PlayerEntity) (Object)this)) ci.cancel();
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "isBlockBreakingRestricted")
    public boolean boundless$isBlockBreakingRestricted(boolean original) {
        return original || (HeroUtils.combatModeEnabled((PlayerEntity) (Object)this));
    }
}
