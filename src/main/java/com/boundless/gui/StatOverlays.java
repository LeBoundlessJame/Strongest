package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.GUIUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.MeterUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Math;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatOverlays {
    public static final Identifier HEALTH_BAR_BACKGROUND = BoundlessAPI.hudPNG("health_bar_background");
    public static final Identifier HEALTH_BAR_PROGRESS = BoundlessAPI.hudPNG("health_bar_progress");
    public static final Identifier SWORD = BoundlessAPI.hudPNG("sword");
    public static final Identifier VANILLA_MODE = BoundlessAPI.hudPNG("vanilla_mode");
    public static final Identifier SHIELD = BoundlessAPI.hudPNG("shield");
    public static final Identifier HOTBAR = BoundlessAPI.hudPNG("hotbar");
    public static final Identifier HEALTH = BoundlessAPI.hudPNG("health");


    public static void renderHealthOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 113;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int healthProgress = (int) Math.lerp(0, maxWidth, player.getHealth() / player.getMaxHealth());

        ArrayList<Float> colors = GUIUtils.hexToUnitColor("f23d3d");
        RenderSystem.setShaderColor(colors.get(0), colors.get(1), colors.get(2), 1.0f);

        context.drawTexture(HEALTH, x, y, 0, 0, 0, healthProgress, 10, 66, 10);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        String healthPercentage = String.format("%.1f / %.1f", player.getHealth() + player.getAbsorptionAmount(), player.getMaxHealth());
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x + (maxWidth / 2) - (textWidth / 2) + 12;
        context.drawText(client.textRenderer, healthPercentage, textPos, y - 8, 0xf23d3d, true);
    }

    public static void renderCursedEnergyOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int cursedEnergy = MeterUtils.getRemainingMeter(player, 10000);
        // Todo: only hard coding this for now, but revisit it later
        int maxCursedEnergy = 10000;

        int x = context.getScaledWindowWidth() / 2 + 11;
        int y = context.getScaledWindowHeight() - 39;
        int maxWidth = 80;
        int healthProgress = maxWidth * cursedEnergy / maxCursedEnergy;

        ArrayList<Float> colors = GUIUtils.hexToUnitColor("1bc7b6");
        RenderSystem.setShaderColor(colors.get(0), colors.get(1), colors.get(2), 1.0f);

        context.drawTexture(HEALTH_BAR_BACKGROUND, x, y, 0, 0, 0, 80, 9, 80, 9);
        context.drawTexture(HEALTH_BAR_PROGRESS, x, y, 0, 0, 0, healthProgress, 9, 80, 9);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        String meterPercentage = cursedEnergy + " / " + maxCursedEnergy;
        int textWidth = client.textRenderer.getWidth(meterPercentage);
        int textPos = x + (maxWidth / 2) - (textWidth / 2);
        context.drawText(client.textRenderer, meterPercentage, textPos, y - 8, 0x1bc7b6, true);
    }

    public static void renderCombatModeIndicator(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        ItemStack stack = HeroUtils.getHeroStack(client.player);

        int x = context.getScaledWindowWidth() / 2 - 10;
        int y = context.getScaledWindowHeight() - 50;

        if (stack.getOrDefault(DataComponentRegistry.COMBAT_MODE_ENABLED, false) && stack.getOrDefault(DataComponentRegistry.BLOCK_TICKS, 0) <= 0) {
            context.drawTexture(SWORD, x, y, 0, 0, 0, 22, 22, 22, 22);
        }
    }

    public static void renderBlockIndicator(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        ItemStack stack = HeroUtils.getHeroStack(client.player);

        int x = context.getScaledWindowWidth() / 2 - 11;
        int y = context.getScaledWindowHeight() - 51;

        if (stack.get(DataComponentRegistry.BLOCK_HP) == null) return;
        String blockHP = String.valueOf((int) Math.floor(stack.get(DataComponentRegistry.BLOCK_HP)));
        int padX = 0;

        if (stack.getOrDefault(DataComponentRegistry.BLOCK_TICKS, 0) > 0) {
            // Todo: I also hate this, I will one day come back and make it not hard coded
            if (blockHP.length() == 3) padX = 2;
            else if (blockHP.length() == 2) padX = 5;
            else if (blockHP.length() == 1) padX = 8;

            context.drawTexture(SHIELD, x, y, 0, 0, 0, 22, 22, 22, 22);
            context.drawText(client.textRenderer, Text.of(String.valueOf(blockHP)), x + padX, y + 6, 0xffffff, true);
        }
    }

    public static void renderHotbar(DrawContext context) {
        int x = (context.getScaledWindowWidth() - 260) / 2;
        context.drawTexture(HOTBAR, x, context.getScaledWindowHeight() - 40, 0, 0, 260, 40, 260, 40);
        renderSkillSlots(MinecraftClient.getInstance(), context);
    }

    public static void renderSkillSlots(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));

        int offset = 1;
        for (Map.Entry<String, Identifier> entry : abilityLoadout.entrySet()) {
            Ability ability = AbilityRegistry.getAbilityFromID(entry.getValue());
            if (ability == null || ability.getSkillSlot() == null || ability.getSkillSlotTexture() == null) continue;
            String boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.getKey()).getBoundKeyLocalizedText().getString();

            int x = (context.getScaledWindowWidth() - 244) / 2;
            context.drawTexture(ability.getSkillSlotTexture(), x + (offset * 16), context.getScaledWindowHeight() - 20, 0, 0, 16, 16, 16, 16);

            //context.drawText(client.textRenderer, boundKey, x + (offset * 16) + 5, context.getScaledWindowHeight() - 24, 0xffffff, true);
            offset += 1;
        }
    }
}
