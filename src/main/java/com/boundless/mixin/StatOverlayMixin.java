package com.boundless.mixin;

import com.boundless.client.StatOverlays;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class StatOverlayMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract void renderExperienceBar(DrawContext context, int x);

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void boundless$renderStatusBars(DrawContext context, CallbackInfo ci) {
        if (!cancelVanillaRendering(client)) return;
        ci.cancel();

        StatOverlays.renderHealthOverlay(client, context);
        StatOverlays.renderHealthText(client, context);
        renderExperienceBar(context, context.getScaledWindowWidth() / 2 - 91);
    }

    // Todo: prevent hotbar item rendering too, and rework health / ce visuals etc.
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void boundless$cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!cancelVanillaRendering(client)) return;
        StatOverlays.renderHotbar(context);
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void boundless$cancelHotbar(DrawContext context, int x, CallbackInfo ci) {
        if (!cancelVanillaRendering(client)) return;
        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void boundless$cancelExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!cancelVanillaRendering(client)) return;
        ci.cancel();
    }

    private boolean cancelVanillaRendering(MinecraftClient client) {
        return HeroUtils.isHero(client.player) && client.player != null;
    }
}
