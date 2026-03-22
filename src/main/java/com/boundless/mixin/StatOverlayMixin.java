package com.boundless.mixin;

import com.boundless.gui.StatOverlays;
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
public class StatOverlayMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    public void boundless$renderStatusBars(DrawContext context, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player)) return;
        if (!HeroUtils.combatModeEnabled(client.player)) return;
        ci.cancel();
        StatOverlays.renderHealthOverlay(client, context);
        StatOverlays.renderCursedEnergyOverlay(client, context);
        StatOverlays.renderBlockIndicator(client, context);
    }

    // Todo: prevent hotbar item rendering too, and rework health / ce visuals etc.
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (!HeroUtils.combatModeEnabled(client.player)) return;
        ci.cancel();
        StatOverlays.renderHotbar(context);
    }

    @Inject(method = "renderHotbarItem", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (!HeroUtils.combatModeEnabled(client.player)) return;
        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelHotbar(DrawContext context, int x, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (!HeroUtils.combatModeEnabled(client.player)) return;
        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    public void boundless$cancelExperienceLevel(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!HeroUtils.isHero(client.player) || client.player == null) return;
        if (!HeroUtils.combatModeEnabled(client.player)) return;
        ci.cancel();
    }
}
