package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.HeroHUD;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BlackSparksHUD {
    public static Identifier MOUSE =  BoundlessAPI.hudPNG("mouse");
    public static Identifier DIVERGENT_FIST =  BoundlessAPI.hudPNG("divergent_fist");
    public static Identifier BLACK_FLASH =  BoundlessAPI.hudPNG("black_flash");

    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null || !HeroUtils.isHero(minecraftClient.player)) return;
        PlayerEntity player = minecraftClient.player;

        HeroHUD.render(drawContext, renderTickCounter);
        float scale = 2f;
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, scale);

        if (!canBlackFlash(player)) {
            drawContext.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        drawContext.drawTexture(BLACK_FLASH, (int) ((float) drawContext.getScaledWindowWidth() / 3 / scale), (int) (((float) drawContext.getScaledWindowHeight() / 2) / scale), 0, 0, 16, 16, 16, 16);
        drawContext.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        drawContext.drawTexture(DIVERGENT_FIST, (int) ((float) drawContext.getScaledWindowWidth() / 1.65 / scale), (int) (((float) drawContext.getScaledWindowHeight() / 2) / scale), 0, 0, 16, 16, 16, 16);
        matrices.pop();
    }

    public static boolean canBlackFlash(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        long currentTime = player.getWorld().getTime();
        long blackFlashTimestamp = stack.getOrDefault(BlackSparksHero.BLACK_FLASH_TIMESTAMP, currentTime);
        long timeWindow = 3;
        return currentTime >= blackFlashTimestamp - timeWindow && currentTime <= blackFlashTimestamp + timeWindow;
    }
}
