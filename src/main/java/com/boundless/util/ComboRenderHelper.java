package com.boundless.util;

import com.boundless.combat.Combo;
import com.boundless.hero.shrine_hero.ShrineHero;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ComboRenderHelper {
    public static void renderPlayerCombos(MinecraftClient client, DrawContext context) {
        int width = context.getScaledWindowWidth();

        for (Combo combo: ShrineHero.COMBOS) {
            GUIUtils.drawLabelledText(context, client, combo.comboName, 0xff0000, width - 50, 10, 2, 2, 0.4f);
        }
    }
}
