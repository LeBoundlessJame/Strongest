package com.boundless.client.hud;

import com.boundless.BoundlessAPI;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.util.GUIUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;

public class StatOverlays {
    public static final Identifier HOTBAR = BoundlessAPI.hudPNG("hotbar");
    public static final Identifier HOTBAR_LEFT = BoundlessAPI.hudPNG("hotbar_left");
    public static final Identifier HOTBAR_RIGHT = BoundlessAPI.hudPNG("hotbar_right");
    public static final Identifier HEALTH = BoundlessAPI.hudPNG("health");
    public static final Identifier HEALTH_WHITE = BoundlessAPI.hudPNG("health_white");
    public static final Identifier CURSED_ENERGY = BoundlessAPI.hudPNG("cursed_energy");
    public static final Identifier CURSED_ENERGY_WHITE = BoundlessAPI.hudPNG("cursed_energy_white");

    private static final int TRAIL_DELAY_TICKS = 4;
    private static final int TRAIL_TRANSITION_TICKS = 4;

    private static boolean hasPrevHealth = false;
    private static float prevHealth;
    private static float trailingHealth;
    private static float trailingHealthStart;
    private static int lastDamageTimestamp;

    public static void renderHealthOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        float health = player.getHealth();

        if (!hasPrevHealth) {
            prevHealth = health;
            trailingHealth = health;
            hasPrevHealth = true;
        }

        if (health < prevHealth) {
            trailingHealth = prevHealth;
            trailingHealthStart = prevHealth;
            lastDamageTimestamp = player.age;
        }

        prevHealth = health;

        if (lastDamageTimestamp >= 0) {
            int ticksSinceLastDamage = player.age - lastDamageTimestamp;

            if (ticksSinceLastDamage > TRAIL_DELAY_TICKS && trailingHealth > health) {
                float progress = MathHelper.clamp((ticksSinceLastDamage - TRAIL_DELAY_TICKS) / (float) TRAIL_TRANSITION_TICKS, 0.0f, 1.0f);
                trailingHealth = MathHelper.lerp(progress, trailingHealthStart, health);
            }
        }

        int x = context.getScaledWindowWidth() / 2 - 107;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int healthProgress = (int) Math.lerp(0, maxWidth, player.getHealth() / player.getMaxHealth());
        int trailingHealthProgress = (int) Math.lerp(0, maxWidth, trailingHealth / player.getMaxHealth());

        if (trailingHealthProgress > healthProgress) {
            context.drawTexture(HEALTH_WHITE, x, y, 0, 0, 0, trailingHealthProgress, 10, 66, 10);
        }

        if (player.isCreative()) return;
        context.drawTexture(HEALTH, x, y, 0, 0, 0, healthProgress, 10, 66, 10);
    }

    public static void renderCursedEnergyOverlay(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int cursedEnergy = CursedEnergyManager.getCursedEnergy(player);
        int maxCursedEnergy = CursedEnergyManager.getMaxCursedEnergy(player);

        int x = context.getScaledWindowWidth() / 2 + 40;
        int y = context.getScaledWindowHeight() - 34;
        int maxWidth = 66;
        int cursedEnergyWidth = maxWidth * cursedEnergy / maxCursedEnergy;

        context.drawTexture(CURSED_ENERGY, x, y, 0, 0, 0, cursedEnergyWidth, 10, 66, 10);
        String healthPercentage = cursedEnergy + " / " + maxCursedEnergy;
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x - (textWidth / 2) + 25;
        GUIUtils.drawLabelledOutlinedText(context, client, healthPercentage, 0x1bc7b6, textPos, y - 8, 0, 0, 0.0f);
    }

    public static void renderHotbar(MinecraftClient client, DrawContext context) {
        int x = context.getScaledWindowWidth() / 2 - 130;
        int y = context.getScaledWindowHeight() - 60;

        Identifier hotbar = HOTBAR;

        if (!client.player.getOffHandStack().isEmpty()) {
            hotbar = client.player.getMainArm() == Arm.LEFT ? HOTBAR_LEFT : HOTBAR_RIGHT;
        }

        context.drawTexture(hotbar, x, y, 0, 0, 260, 60, 260, 60);
    }

    public static void renderHealthText(MinecraftClient client, DrawContext context) {
        PlayerEntity player = client.player;
        if (player == null) return;

        int x = context.getScaledWindowWidth() / 2 - 120;
        int y = context.getScaledWindowHeight() - 36;

        String healthPercentage = String.format("%.1f / %.1f", player.getHealth() + player.getAbsorptionAmount(), player.getMaxHealth());
        int textWidth = client.textRenderer.getWidth(healthPercentage);
        int textPos = x - (textWidth / 2) + 56;

        int healthTextColor = player.isCreative() ? 0xffffff : 0xf23d3d;
        GUIUtils.drawLabelledOutlinedText(context, client, healthPercentage, healthTextColor, textPos, y - 8, 0, 0, 0.0f);
    }
}
