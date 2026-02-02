package com.boundless.gui;

import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeroHUD {
    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || !HeroUtils.isHero(client.player)) return;

        int padX = 2;
        int padY = 2;
        int x = client.textRenderer.getWidth(formattedAbilityString(client));
        int y = client.textRenderer.fontHeight + padY;

        drawContext.fill(10, 10, 10 + (padX * 2) + x, y * 2, client.options.getTextBackgroundColor(0.4F));
        drawContext.drawText(client.textRenderer, formattedAbilityString(client), 12, 12, 0xffffffff, false);
    }

    public static String formattedAbilityString(MinecraftClient client) {
        if (client.player == null) return "";
        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));
        List<Map.Entry<String, Identifier>> entryList = new LinkedList<>(abilityLoadout.entrySet());
        String boundKeyText = "";

        for (int i = 0; i < abilityLoadout.size(); i++) {
            Ability ability = AbilityRegistry.getAbilityFromID(entryList.get(i).getValue());
            if (ability == null || ability.isHide()) continue;
            boundKeyText = KeybindingUtils.getKeyBindingFromTranslation(entryList.get(i).getKey()).getBoundKeyLocalizedText().getString();
        }
        return boundKeyText + " - " + "Suplex";
    }
}
