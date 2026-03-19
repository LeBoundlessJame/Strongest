package com.boundless.mixin;

import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.VFXUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class StunRemovalMixin {
    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    protected void boundless$onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
        if (effect.getEffectType() != StatusEffectRegistry.STUN) return;
        VFXUtils.destroyEffectInstance("stun", ((LivingEntity)(Object)this).getId());
    }
}
