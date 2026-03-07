package com.boundless.combat;

import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.function.BiConsumer;

// Todo: in the future, make it list based potentially
public class Combo {
    public String sequence;
    public BiConsumer<PlayerEntity, HeroActionEntity> logic;
    public ComponentType<String> component;

    public Combo(String sequence, BiConsumer<PlayerEntity, HeroActionEntity> logic) {
        this.sequence = sequence;
        this.logic = logic;
        this.component = DataComponentRegistry.registerString(this.sequence);
    }

    public void updateAndEvaluateCombo(PlayerEntity player, String attack) {
        if (matchesTargetCombo(player)) {
            logic.accept(player, null);
            resetProgress(player);
        } else if (Objects.equals(attack, requiredAttack(player))) {
            updateProgress(player, attack);
        } else {
            resetProgress(player);
        }
    }

    public boolean matchesTargetCombo(PlayerEntity player) {
        return this.getProgress(player).equals(this.sequence);
    }

    public void updateProgress(PlayerEntity player, String attack) {
        HeroUtils.getHeroStack(player).set(this.component, getProgress(player) + attack);
    }

    public String getProgress(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(this.component, "");
    }

    public void resetProgress(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(component, "");
    }

    public String requiredAttack(PlayerEntity player) {
        if (getProgress(player).isEmpty()) return String.valueOf(this.sequence.charAt(0));
        return String.valueOf(this.sequence.charAt(getProgress(player).length() - 1));
    }
}
