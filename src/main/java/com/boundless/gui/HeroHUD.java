package com.boundless.gui;

import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeroHUD {
    public static void render(DrawContext context, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || !HeroUtils.isHero(client.player)) return;
        renderKeybindAbilities(client, context);
    }

    public static void renderKeybindAbilities(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));

        int offset = 1;
        for (Map.Entry<String, Identifier> entry : abilityLoadout.entrySet()) {
            Ability ability = AbilityRegistry.getAbilityFromID(entry.getValue());
            if (ability == null || ability.isHide() || ability.getDisplayString() == null) continue;
            String boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.getKey()).getBoundKeyLocalizedText().getString();
            renderKeybindAbility(client, context, offset, boundKey, ability.getDisplayString());
            offset += 1;
        }
    }

    public static void renderKeybindAbility(MinecraftClient client, DrawContext context, int yOffset, String boundKey, String abilityString) {
        int padX = 2;
        int padY = 2;

        int x = client.textRenderer.getWidth(formattedAbilityString(boundKey, abilityString));
        int y = (10 + padY) * yOffset - padY;

        context.fill(10, y, 10 + (padX * 2) + x, y + 12, client.options.getTextBackgroundColor(0.4F));
        context.drawText(client.textRenderer, formattedAbilityString(boundKey, abilityString), 12, y + padY, 0xffffffff, false);
    }

    public static String formattedAbilityString(String boundKey, String abilityString) {
        return boundKey + " - " + abilityString;
    }
}
