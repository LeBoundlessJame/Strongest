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
import net.minecraft.util.math.MathHelper;

public class BlackSparksHUD {
    public static Identifier MOUSE =  BoundlessAPI.hudPNG("mouse");
    public static Identifier DIVERGENT_FIST =  BoundlessAPI.hudPNG("divergent_fist");
    public static Identifier BLACK_FLASH =  BoundlessAPI.hudPNG("black_flash");

    public static Identifier METER_FRAME = BoundlessAPI.hudPNG("meter_frame");
    public static Identifier METER = BoundlessAPI.hudPNG("meter");

    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null || !HeroUtils.isHero(minecraftClient.player)) return;
        PlayerEntity player = minecraftClient.player;

        HeroHUD.render(drawContext, renderTickCounter);

        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.push();
        float scale = 1.0f;
        matrixStack.scale(scale, scale, scale);
        // Todo: My use of magic numbers here is brutal. Come back later to add some clarity
        int meterHeight = MathHelper.clamp(MathHelper.lerp((player.getHealth() / player.getMaxHealth()), 119, 0), 0, 119);
        drawContext.drawTexture(METER_FRAME, (int) (5 / scale), (int) ((20) / scale), 0f, 127, 11, 127, 11, 127);
        drawContext.drawTexture(METER, (int) (5 / scale), (int) ((24 + meterHeight) / scale), 0f, 131, 11, 119 - meterHeight, 11, 127);
        matrixStack.pop();
    }
}
