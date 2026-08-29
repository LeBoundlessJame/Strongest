package com.boundless.mixin;

import mod.chloeprime.aaaparticles.client.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Debug.class, remap = false)
public class AAADebugPreventionMixin {
    @Inject(at = @At("HEAD"), cancellable = true, method = "registerDebugHooks")
    public void boundless$preventDebugHooks(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}