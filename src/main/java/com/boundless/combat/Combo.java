package com.boundless.combat;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;

public abstract class Combo {
    public String sequence;
    public String comboName;
    public ComponentType<String> component;

    public Combo(String sequence, String comboName) {
        this.sequence = sequence;
        this.comboName = comboName;
        this.component = DataComponentRegistry.registerString(this.sequence);
    }

    public void updateAndEvaluateCombo(PlayerEntity player, String attack) {
        if (this.getProgress(player).length() > this.sequence.length()) resetProgress(player);

        if (matchesTargetCombo(player, attack)) {
            this.executeCombo(player);
            resetProgress(player);
        } else if (Objects.equals(attack, requiredAttack(player))) {
            updateProgress(player, attack);
        } else {
            resetProgress(player);
        }
    }

    public boolean matchesTargetCombo(PlayerEntity player, String attack) {
        if (this.getProgress(player).length() == this.sequence.length()) return this.getProgress(player).equals(this.sequence);
        return (this.getProgress(player) + attack).equals(this.sequence);
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
        return String.valueOf(this.sequence.charAt(getProgress(player).length()));
    }

    public abstract void executeCombo(PlayerEntity player);
}
