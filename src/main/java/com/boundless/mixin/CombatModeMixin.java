package com.boundless.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftClient.class)
public class CombatModeMixin {
    /*
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void boundless$doAttack(CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (HeroUtils.isHero(player) && HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE, true)) cir.cancel();
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void boundless$doItemUse(CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (HeroUtils.isHero(player) && HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE, true)) ci.cancel();
    }
     */
}
