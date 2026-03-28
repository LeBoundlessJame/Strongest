package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.MeterUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;

import java.util.LinkedHashMap;
import java.util.Map;

public class StatOverlays {
    public static final Identifier SHIELD = BoundlessAPI.hudPNG("shield");
    public static final Identifier HOTBAR = BoundlessAPI.hudPNG("hotbar");
    public static final Identifier HEALTH = BoundlessAPI.hudPNG("health");
    public static final Identifier CURSED_ENERGY = BoundlessAPI.hudPNG("cursed_energy");

    public static void renderHealthOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 113;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int healthProgress = (int) Math.lerp(0, maxWidth, player.getHealth() / player.getMaxHealth());

        context.drawTexture(HEALTH, x, y, 0, 0, 0, healthProgress, 10, 66, 10);

        String healthPercentage = String.format("%.1f / %.1f", player.getHealth() + player.getAbsorptionAmount(), player.getMaxHealth());
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x + (maxWidth / 2) - (textWidth / 2) + 12;
        context.drawText(client.textRenderer, healthPercentage, textPos, y - 8, 0xf23d3d, true);
    }

    public static void renderCursedEnergyOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int cursedEnergy = MeterUtils.getRemainingMeter(player);
        int maxCursedEnergy = MeterUtils.getMaxCE(player);

        int x = context.getScaledWindowWidth() / 2 + 35;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int healthProgress = maxWidth * cursedEnergy / maxCursedEnergy;

        context.drawTexture(CURSED_ENERGY, x, y, 0, 0, 0, healthProgress, 10, 66, 10);

        String meterPercentage = cursedEnergy + " / " + maxCursedEnergy;
        int textWidth = client.textRenderer.getWidth(meterPercentage);
        int textPos = x + (maxWidth / 2) - (textWidth / 2) - 12;
        context.drawText(client.textRenderer, meterPercentage, textPos, y - 8, 0x1bc7b6, true);
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
        LinkedHashMap<Identifier, Long> abilityCooldowns = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, new LinkedHashMap<>()));

        for (Map.Entry<String, Identifier> entry : abilityLoadout.entrySet()) {
            Ability ability = AbilityRegistry.getAbilityFromID(entry.getValue());
            if (ability == null || ability.getSkillSlot() == null || ability.getIcon() == null) continue;
            int x = (context.getScaledWindowWidth() - 244) / 2;
            int y = context.getScaledWindowHeight() - 20;
            int pos = x + (ability.getSkillSlot() * 16);
            if (ability.getSkillSlot() > 1) pos += (7 * (ability.getSkillSlot() - 1));

            context.drawTexture(ability.getIcon(), pos, y, 0, 0, 16, 16, 16, 16);

            long endTick = abilityCooldowns.getOrDefault(ability.getId(), 0L);
            long currentTick = client.player.getWorld().getTime();
            int abilityCooldown = ability.getCooldown();

            float f = (float) (endTick - currentTick);
            float g = (float) abilityCooldown;

            if (g > 0) {
                float cooldownProgress = MathHelper.clamp(f / g, 0, 1);
                int displayProgress = (int) Math.lerp(0, 16, cooldownProgress);

                context.fill(pos, y + 16 - displayProgress, pos + 16, y + 16, 0x64FFFFFF);
            }


            /*
            String boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.getKey()).getBoundKeyLocalizedText().getString();
            context.drawText(client.textRenderer, boundKey, pos, context.getScaledWindowHeight() - 8, 0xffffff, true);
            */
        }
    }

    public static void renderHealthText(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 113;
        int y = context.getScaledWindowHeight() - 34;

        String healthPercentage = String.format("%.1f / %.1f", player.getHealth() + player.getAbsorptionAmount(), player.getMaxHealth());
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x - (textWidth / 2) + 56;
        context.drawText(client.textRenderer, healthPercentage, textPos, y - 8, 0xf23d3d, true);
    }

    public static void renderCEText(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int cursedEnergy = MeterUtils.getRemainingMeter(player);
        int maxCursedEnergy = MeterUtils.getMaxCE(player);

        int x = context.getScaledWindowWidth() / 2 + 52;
        int y = context.getScaledWindowHeight() - 42;

        String meterPercentage = cursedEnergy + " / " + maxCursedEnergy;
        int textWidth = client.textRenderer.getWidth(meterPercentage);
        int textPos = x - (textWidth / 2);
        context.drawText(client.textRenderer, meterPercentage, textPos, y - 8, 0x1bc7b6, true);
    }
}
