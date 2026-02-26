package com.boundless.mixin;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class CombatModeMixin {
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void boundless$doAttack(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE_ENABLED, true)) cir.cancel();
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void boundless$doItemUse(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE_ENABLED, true)) ci.cancel();
    }
}
