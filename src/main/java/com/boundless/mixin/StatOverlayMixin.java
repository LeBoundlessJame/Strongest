package com.boundless.mixin;

import com.boundless.gui.StatOverlays;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class StatOverlayMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    public void boundless$renderStatusBars(DrawContext context, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player)) return;
        ci.cancel();
        StatOverlays.renderHealthOverlay(client, context);
        StatOverlays.renderCursedEnergyOverlay(client, context);
    }
}
