package com.boundless.mixin;

import com.boundless.util.HeroUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class HandSwingMixin {
    @Inject(at = @At("HEAD"), method = "swingHand(Lnet/minecraft/util/Hand;)V", cancellable = true)
    public void boundless$attack(Hand hand, CallbackInfo ci) {
        if (((LivingEntity)(Object)this) instanceof PlayerEntity player) {
            if (HeroUtils.isHero(player)) ci.cancel();
        }
    }
}
