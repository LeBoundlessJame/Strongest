package com.boundless.mixin;

import com.boundless.BoundlessAPI;
import com.boundless.effect.StunEffect;
import com.boundless.util.VFXUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class StunVfxRemovalMixin {
    @Inject(at = @At("HEAD"), method = "onStatusEffectRemoved")
    protected void boundless$onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        /* TODO: FIX
        if (effect.getEffectType().value() instanceof StunEffect) {
            VFXUtils.destroyEffectInstance("stun", ((LivingEntity)(Object)this).getId());
        }
         */
    }
}
