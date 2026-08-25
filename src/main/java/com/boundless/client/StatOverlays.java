package com.boundless.client;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.GUIUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatOverlays {
    public static final Identifier HOTBAR = BoundlessAPI.hudPNG("hotbar");
    public static final Identifier HOTBAR_LEFT = BoundlessAPI.hudPNG("hotbar_left");
    public static final Identifier HOTBAR_RIGHT = BoundlessAPI.hudPNG("hotbar_right");
    public static final Identifier HEALTH = BoundlessAPI.hudPNG("health");
    public static final Identifier CURSED_ENERGY = BoundlessAPI.hudPNG("cursed_energy");

    public static void renderHealthOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 107;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int healthProgress = (int) Math.lerp(0, maxWidth, player.getHealth() / player.getMaxHealth());

        context.drawTexture(HEALTH, x, y, 0, 0, 0, healthProgress, 10, 66, 10);
    }

    public static void renderHotbar(MinecraftClient client, DrawContext context) {
        int x = context.getScaledWindowWidth() / 2 - 130;
        int y = context.getScaledWindowHeight() - 40;

        Identifier hotbar = HOTBAR;

        if (!client.player.getOffHandStack().isEmpty()) {
            hotbar = client.player.getMainArm() == Arm.LEFT ? HOTBAR_LEFT : HOTBAR_RIGHT;
        }

        context.drawTexture(hotbar, x, y, 0, 0, 260, 40, 260, 40);
    }

    public static void renderHealthText(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 120;
        int y = context.getScaledWindowHeight() - 36;

        String healthPercentage = String.format("%.1f / %.1f", player.getHealth() + player.getAbsorptionAmount(), player.getMaxHealth());
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x - (textWidth / 2) + 56;
        GUIUtils.drawLabelledOutlinedText(context, client, healthPercentage, 0xf23d3d, textPos, y - 8, 0, 0, 0.0f);
    }
}
