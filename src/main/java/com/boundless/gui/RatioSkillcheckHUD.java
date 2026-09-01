package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class RatioSkillcheckHUD {
    private static final Identifier RATIO_SKILLCHECK = BoundlessAPI.hudPNG("ratio_skillcheck");
    private static final Identifier RATIO_CURSOR = BoundlessAPI.hudPNG("ratio_cursor");

    // this is to prevent mismatch
    private static int HORIZONTAL_OFFSET = 9;

    // yes, i'm using hard coded pixel positions. yes, i know it's slightly cooked, but it works guys fr
    private static final int RATIO_LINES_START = 68;
    private static final int RATIO_LINES_END = 190;
    private static final int RATIO_TARGET = 154;

    public static void render(MinecraftClient client, DrawContext context, RenderTickCounter renderTickCounter) {
        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(client.player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        int x = context.getScaledWindowWidth() / 2 - 130;
        int y = context.getScaledWindowHeight() - 120;

        context.drawTexture(RATIO_SKILLCHECK, x, y, 0, 0, 260, 60, 260, 60);

        float currentTick = client.player.getWorld().getTime() + renderTickCounter.getTickDelta(false);

        float speed = (RATIO_TARGET - RATIO_LINES_START) / (float) (skillcheck.targetTick() - skillcheck.startTick());
        float distance = (currentTick - skillcheck.startTick()) * speed;
        int cursorX = x + RATIO_LINES_START + Math.round(distance) - HORIZONTAL_OFFSET;

        if (cursorX < x + RATIO_LINES_END) {
            context.drawTexture(RATIO_CURSOR, cursorX, y + 20, 0, 0, 16, 16, 16, 16);
        }
    }
}
