package com.boundless.mixin;

import com.boundless.gui.StatOverlays;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class StatOverlayMixin {

    @Shadow @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    public void boundless$renderStatusBars(DrawContext context, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player)) return;
        ci.cancel();
        StatOverlays.renderHealthOverlay(client, context);
        StatOverlays.renderCursedEnergyOverlay(client, context);
        StatOverlays.renderCombatModeIndicator(client, context);
        StatOverlays.renderBlockIndicator(client, context);
    }

    /*
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (HeroUtils.combatModeEnabled(client.player) && !client.player.isInCreativeMode()) ci.cancel();
        StatOverlays.renderHotbar(context);
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, int x, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (HeroUtils.combatModeEnabled(client.player) && !client.player.isInCreativeMode()) ci.cancel();
    }

     */
}
