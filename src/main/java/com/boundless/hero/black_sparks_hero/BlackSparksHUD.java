package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.gui.HeroHUD;
import com.boundless.hero.OldHeroHUD;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.GUIUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.ShaderAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class BlackSparksHUD {
    public static Identifier METER_FRAME_HORIZONTAL = BoundlessAPI.hudPNG("meter_frame_horizontal");
    public static Identifier METER_HORIZONTAL = BoundlessAPI.hudPNG("meter_horizontal");

    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null || !HeroUtils.isHero(minecraftClient.player)) return;
        PlayerEntity player = minecraftClient.player;
        HeroHUD.render(drawContext, renderTickCounter);

        if (minecraftClient.player.hasStatusEffect(StatusEffectRegistry.IMPACT_FRAME_EFFECT)) {
            ((ShaderAccessor)minecraftClient.gameRenderer).boundless$loadShader(Identifier.of(BoundlessAPI.MOD_ID, "shaders/post/black_flash.json"));
        } else if (minecraftClient.player.hasStatusEffect(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT)) {
            ((ShaderAccessor)minecraftClient.gameRenderer).boundless$loadShader(Identifier.of(BoundlessAPI.MOD_ID, "shaders/post/boogie_woogie.json"));
        } else {
            if (minecraftClient.gameRenderer.getPostProcessor() != null) {
                ((ShaderAccessor)minecraftClient.gameRenderer).boundless$disablePostProcessor();
            }
        }

        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        float scale = 1.0f;
        matrixStack.scale(scale, scale, scale);

        // Todo: Fix this rendering behind chat
        if (BlackFlashAbility.blackFlashMinigameActive(player)) {
            matrixStack.push();
            matrixStack.translate(0, 0, 10000);
            ItemStack stack = HeroUtils.getHeroStack(player);

            float target = stack.getOrDefault(BlackSparksHero.MINIGAME_END_TIMESTAMP, 0L);
            float start = stack.getOrDefault(BlackSparksHero.MINIGAME_START_TIMESTAMP, 0L);
            float current = player.getWorld().getTime() - start;
            float progress = current / (target - start);

            int meterWidth = MathHelper.clamp(MathHelper.lerp((progress), 119, 0), 0, 119);

            drawContext.drawTexture(METER_FRAME_HORIZONTAL, drawContext.getScaledWindowWidth() / 2 - 70, drawContext.getScaledWindowHeight() - 62, 0, 0, 127, 11, 127, 11);
            drawContext.drawTexture(METER_HORIZONTAL, drawContext.getScaledWindowWidth() / 2 - 70, drawContext.getScaledWindowHeight() - 62, 0, 0, meterWidth, 11, 127, 11);

            GUIUtils.drawLabelledText(drawContext, minecraftClient, getTargetComboString(player), 0xfffc5454, drawContext.getScaledWindowWidth() / 2 - minecraftClient.textRenderer.getWidth(getTargetComboString(player)) / 2, drawContext.getScaledWindowHeight() / 2 + 10, 2, 2);
            GUIUtils.drawLabelledText(drawContext, minecraftClient, getComboString(player), 0xff1bc7b6, drawContext.getScaledWindowWidth() / 2 - minecraftClient.textRenderer.getWidth(getTargetComboString(player)) / 2, drawContext.getScaledWindowHeight() / 2 + 10, 2, 2, 0.0f);

            /*
            GUIUtils.drawOutlinedText(drawContext, minecraftClient, getTargetComboString(player), drawContext.getScaledWindowWidth() / 2 - 70, drawContext.getScaledWindowHeight() / 2 + 10, colors);
            colors = GUIUtils.hexToUnitColor("1bc7b6");
            GUIUtils.drawOutlinedText(drawContext, minecraftClient, getComboString(player), drawContext.getScaledWindowWidth() / 2 - 70, drawContext.getScaledWindowHeight() / 2 + 10, colors);
            */
           matrixStack.pop();

        }

        matrixStack.pop();
    }

    public static String getComboString(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        String comboProgress = stack.getOrDefault(BlackSparksHero.CURRENT_MINIGAME_COMBO, "");

        if (comboProgress.isEmpty()) return "";
        return "Black Flash: " + formattedCombo(comboProgress);
    }

    public static String getTargetComboString(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        String combo = stack.getOrDefault(BlackSparksHero.TARGET_MINIGAME_COMBO, "");
        return "Black Flash: " + formattedCombo(combo);
    }

    public static String formattedCombo(String combo) {
        return combo.replaceAll("(.)", "$1 -> ").replaceAll(" -> $", "");
    }
}
