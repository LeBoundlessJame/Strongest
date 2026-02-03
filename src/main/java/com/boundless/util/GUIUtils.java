package com.boundless.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class GUIUtils {

    public static void drawLabelledText(DrawContext context, MinecraftClient client, String string, int color, int x, int y, int padX, int padY) {
        int width = client.textRenderer.getWidth(string);
        int height = client.textRenderer.fontHeight;

        context.fill(x, y, x + (padX * 2) + width, y + padY + height, client.options.getTextBackgroundColor(0.4F));
        context.drawText(client.textRenderer, string, x + padX, y + padY, color, false);
    }


    public static void drawOutlinedText(DrawContext drawContext, MinecraftClient instance, String string, int j, int k, ArrayList<Float> colors) {
        drawContext.drawText(instance.textRenderer, string, j + 1, k, 0, false);
        drawContext.drawText(instance.textRenderer, string, j - 1, k, 0, false);
        drawContext.drawText(instance.textRenderer, string, j, k + 1, 0, false);
        drawContext.drawText(instance.textRenderer, string, j, k - 1, 0, false);

        RenderSystem.setShaderColor(colors.get(0), colors.get(1), colors.get(2), 1.0f);
        drawContext.drawText(instance.textRenderer, string, j, k, 0xffffff, false);
        RenderSystem.setShaderColor(1, 1, 1, 1.0f);
    }

    // todo: I found this on https://www.baeldung.com/java-convert-hex-to-rgb so all credit to them for this!
    public static ArrayList<Float> hexToUnitColor(String hexColor) {
        ArrayList<Float> colors = new ArrayList<>();
        colors.add(((Integer.valueOf(hexColor.substring(0, 2), 16)) / 255f));
        colors.add(((Integer.valueOf(hexColor.substring(2, 4), 16)) / 255f));
        colors.add(((Integer.valueOf(hexColor.substring(4, 6), 16)) / 255f));
        return colors;
    }
}
