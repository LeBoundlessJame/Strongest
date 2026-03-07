package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.util.GUIUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Math;

import java.util.ArrayList;

public class HealthDisplayOverlay {
    public static final Identifier HEALTH_BAR_BACKGROUND = BoundlessAPI.identifier("health_bar_background");
    public static final Identifier HEALTH_BAR_PROGRESS = BoundlessAPI.identifier("health_bar_progress");

    public static void renderHealthOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 91;
        int l = context.getScaledWindowHeight() - 39;
        int maxWidth = 80;
        int healthProgress = (int) Math.lerp(0, maxWidth, player.getHealth() / player.getMaxHealth());

        ArrayList<Float> colors = GUIUtils.hexToUnitColor("f23d3d");
        RenderSystem.setShaderColor(colors.get(0), colors.get(1), colors.get(2), 1.0f);

        context.drawGuiTexture(HEALTH_BAR_BACKGROUND, 80, 9, 0, 0, x, l, 80, 9);
        context.drawGuiTexture(HEALTH_BAR_PROGRESS, 80, 9, 0, 0, x, l, healthProgress, 9);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        String healthPercentage = Math.round(player.getHealth() / player.getMaxHealth() * 100) + "%";
        int j = ((context.getScaledWindowWidth() / 2) - 40 - client.textRenderer.getWidth(healthPercentage));
        context.drawText(client.textRenderer, healthPercentage, j, l - 8, 0xf23d3d, false);
    }
}
