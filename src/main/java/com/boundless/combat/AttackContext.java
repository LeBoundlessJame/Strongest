package com.boundless.combat;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

@Getter
public class AttackContext {
    private final List<AttackModifier> activeModifiers;

    public AttackContext(List<AttackModifier> activeModifiers) {
        this.activeModifiers = activeModifiers;
    }

    public void postTrigger(PlayerEntity player) {
        for (AttackModifier modifier: activeModifiers) {
            modifier.postTrigger(player);
        }
    }
}
