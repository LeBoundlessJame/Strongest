package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

public class RatioSkillcheckHUD {
    public static final Identifier RATIO_SKILLCHECK = BoundlessAPI.hudPNG("ratio_skillcheck");

    public static void render(MinecraftClient client, DrawContext context) {
        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(client.player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        int x = context.getScaledWindowWidth() / 2 - 130;
        int y = context.getScaledWindowHeight() / 2 + 20;

        context.drawTexture(RATIO_SKILLCHECK, x, y, 0, 0, 260, 60, 260, 60);
    }
}
