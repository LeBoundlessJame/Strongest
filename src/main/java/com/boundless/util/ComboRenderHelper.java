package com.boundless.util;

import com.boundless.combat.Combo;
import com.boundless.hero.api.HeroData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class ComboRenderHelper {
    public static void renderPlayerCombos(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        HeroData heroData = HeroUtils.getHeroData(client.player);
        if (heroData == null || heroData.getCombos() == null) return;

        for (Combo combo: heroData.getCombos()) {
            int x = context.getScaledWindowWidth() - client.textRenderer.getWidth(getRequiredComboString(combo)) - 12;
            GUIUtils.drawLabelledOutlinedText(context, client, getRequiredComboString(combo), 0xfffc5454, x, 10, 2, 2, 0.4f);
            GUIUtils.drawLabelledOutlinedText(context, client, getCurrentComboString(client.player, combo), 0xff1bc7b6, x, 10, 2, 2, 0f);
        }
    }

    public static String getRequiredComboString(Combo combo) {
        return combo.comboName + ": " + formattedCombo(combo.sequence);
    }

    public static String getCurrentComboString(PlayerEntity player, Combo combo) {
        String comboProgress = HeroUtils.getHeroStack(player).getOrDefault(combo.component, "");
        if (comboProgress.isEmpty()) return "";
        return combo.comboName + ": " + formattedCombo(comboProgress);
    }

    public static String formattedCombo(String combo) {
        // Todo: Just make the combo system list based later on instead of this.
        combo = combo.replace("l", "🗡");
        return combo.replaceAll("(.)", "$1 > ").replaceAll(" > $", "");
    }
}
