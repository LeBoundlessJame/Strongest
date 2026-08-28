package com.boundless.mechanics;

import com.boundless.util.HeroUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ComboManager {
    public static boolean updateCombo(PlayerEntity player, ComponentType<String> component, String input, String sequence) {
        String progress = getProgress(player, component);

        if ((progress + input).equals(sequence)) {
            resetProgress(player, component);
            return true;
        }

        if (progress.length() < sequence.length()) {
            String requiredInput = String.valueOf(sequence.charAt(progress.length()));
            if (input.equals(requiredInput)) {
                updateProgress(player, component, progress + input);
                return false;
            }
        }

        resetProgress(player, component);
        return false;
    }

    public static void updateProgress(PlayerEntity player, ComponentType<String> component, String progress) {
        HeroUtils.getHeroStack(player).set(component, progress);
    }

    public static String getProgress(PlayerEntity player, ComponentType<String> component) {
        return HeroUtils.getHeroStack(player).getOrDefault(component, "");
    }

    public static void resetProgress(PlayerEntity player, ComponentType<String> component) {
        HeroUtils.getHeroStack(player).set(component, "");
    }

    public static String formattedCombo(String combo) {
        return combo.replaceAll("(.)", "$1 - ").replaceAll(" - $", "");
    }

    public static Text formattedComboText(String target, String progress) {
        MutableText text = Text.empty();

        for (int i = 0; i < target.length(); i++) {
            if (i > 0) {
                text.append(Text.literal(" - ")).formatted(Formatting.GRAY);
            }

            Formatting formatting = i < progress.length() ? Formatting.AQUA : Formatting.WHITE;
            text.append(Text.literal(String.valueOf(target.charAt(i))).formatted(formatting));
        }

        return text;
    }
}
