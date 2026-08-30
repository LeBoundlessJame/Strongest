package com.boundless.combat;

import lombok.Getter;

import java.util.List;

@Getter
public class AttackContext {
    private final List<AttackModifier> activeModifiers;

    public AttackContext(List<AttackModifier> activeModifiers) {
        this.activeModifiers = activeModifiers;
    }
}
