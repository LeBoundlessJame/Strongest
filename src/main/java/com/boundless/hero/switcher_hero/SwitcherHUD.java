package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.gui.HeroHUD;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SwitcherHUD {
    public static Identifier METER_FRAME = BoundlessAPI.hudPNG("meter_frame");
    public static Identifier METER = BoundlessAPI.hudPNG("meter");

    public static void render(DrawContext context, RenderTickCounter counter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || !HeroUtils.isHero(client.player)) return;
        HeroHUD.render(context, counter);

        renderTargets(client, context);

        /*
        float scale = 1.0f;
        int meterHeight = MathHelper.clamp(MathHelper.lerp((EnergyUtils.getEnergyMeter(client.player) / 100f), 119, 0), 0, 119);
        context.drawTexture(METER_FRAME, (int) (5 / scale), (int) ((20) / scale), 0f, 127, 11, 127, 11, 127);
        context.drawTexture(METER, (int) (5 / scale), (int) ((24 + meterHeight) / scale), 0f, 131, 11, 119 - meterHeight, 11, 127);
        */
    }


    public static void renderTargets(MinecraftClient client, DrawContext context) {
        if (client.player == null || client.player.getWorld() == null) return;

        Integer primaryID = HeroUtils.getHeroStack(client.player).get(SwitcherHero.PRIMARY_TARGET_ID);
        Integer secondaryID = HeroUtils.getHeroStack(client.player).get(SwitcherHero.SECONDARY_TARGET_ID);

        if (primaryID != null) {
            Entity entity = client.player.getWorld().getEntityById(primaryID);
            if (entity != null) {
                Text displayString = Text.of(entity.getDisplayName().getString() + " (x: " + entity.getBlockX() + ", y: " + entity.getBlockY() + ", z: " + entity.getBlockZ() + ")");
                context.fill(10, 12*6, 14 + client.textRenderer.getWidth(displayString), 12*6 + client.textRenderer.fontHeight + 2, client.options.getTextBackgroundColor(0.4F));
                context.drawText(client.textRenderer, displayString, 12, 12*6 + 2, 0xff00fcff, false);
            }
        }

        if (secondaryID != null) {
            Entity entity = client.player.getWorld().getEntityById(secondaryID);
            if (entity != null) {
                Text displayString = Text.of(entity.getDisplayName().getString() + " (x: " + entity.getBlockX() + ", y: " + entity.getBlockY() + ", z: " + entity.getBlockZ() + ")");
                context.fill(10, 12*7, 14 + client.textRenderer.getWidth(displayString), 12*7 + client.textRenderer.fontHeight + 2, client.options.getTextBackgroundColor(0.4F));
                context.drawText(client.textRenderer, displayString, 12, 12*7 + 2, 0xffff4242, false);
            }
        }
    }
}
