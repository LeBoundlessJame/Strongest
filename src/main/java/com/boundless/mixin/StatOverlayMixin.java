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

    @Shadow
    protected abstract void renderFood(DrawContext context, PlayerEntity player, int top, int right);

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    public void boundless$renderStatusBars(DrawContext context, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player)) return;
        ci.cancel();

        StatOverlays.renderHealthOverlay(client, context);
        StatOverlays.renderHealthText(client, context);
        renderExperienceBar(context, context.getScaledWindowWidth() / 2 - 91);
        int m = context.getScaledWindowWidth() / 2 + 91;
        int n = context.getScaledWindowHeight() - 39;
        renderFood(context, client.player, n, m);
    }

    // Todo: prevent hotbar item rendering too, and rework health / ce visuals etc.
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        StatOverlays.renderHotbar(context);
    }

    @Inject(method = "renderHotbarItem", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, int x, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        ci.cancel();
    }
}
