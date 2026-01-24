package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.HeroHUD;
import com.boundless.hero.black_sparks_hero.BlackFlashAbility;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
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

public class BoogieHUD {
    public static Identifier METER_FRAME = BoundlessAPI.hudPNG("meter_frame");
    public static Identifier METER = BoundlessAPI.hudPNG("meter");

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



        /*
        ArrayList<Float> colors = GUIUtils.hexToUnitColor("fc5454");

        matrixStack.push();
        matrixStack.translate(0, 0, 10000);
        colors = GUIUtils.hexToUnitColor("1bc7b6");
        GUIUtils.drawOutlinedText(drawContext, minecraftClient, "Bruzzah: " + player.getNameForScoreboard(), 5, 5, colors);
        matrixStack.pop();

         */

        int meterHeight = MathHelper.clamp(MathHelper.lerp((player.getHealth() / player.getMaxHealth()), 119, 0), 0, 119);
        drawContext.drawTexture(METER_FRAME, (int) (5 / scale), (int) ((20) / scale), 0f, 127, 11, 127, 11, 127);
        drawContext.drawTexture(METER, (int) (5 / scale), (int) ((24 + meterHeight) / scale), 0f, 131, 11, 119 - meterHeight, 11, 127);
        matrixStack.pop();
    }
}
