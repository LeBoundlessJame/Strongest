package com.boundless.combat;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.function.Consumer;

// Todo: in the future, make it list based potentially
public class Combo {
    public String sequence;
    public Consumer<PlayerEntity> logic;
    public ComponentType<String> component;

    // Todo: might need to be a biconsumer later, I'll see...
    public Combo(String sequence, Consumer<PlayerEntity> logic) {
        this.sequence = sequence;
        this.logic = logic;
        this.component = DataComponentRegistry.registerString(this.sequence);
    }

    public void updateAndEvaluateCombo(PlayerEntity player, String attack) {
        if (matchesTargetCombo(player, attack)) {
            player.sendMessage(Text.of("matches combo!"));
            logic.accept(player);
            resetProgress(player);
        } else if (Objects.equals(attack, requiredAttack(player))) {
            updateProgress(player, attack);
            player.sendMessage(Text.of("update combo progress: now " + getProgress(player)));
        } else {
            player.sendMessage(Text.of("reset progress"));
            resetProgress(player);
        }
    }

    public boolean matchesTargetCombo(PlayerEntity player, String attack) {
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
}
