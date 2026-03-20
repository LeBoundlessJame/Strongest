package com.boundless.ability;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Builder @Getter @Setter
public class Ability {
    private final Consumer<PlayerEntity> abilityLogic;
    private final Predicate<PlayerEntity> abilityConditional;
    private final int cooldown;
    private final Identifier abilityID;
    @Builder.Default
    private final String displayString;
    private final boolean hide;
    @Builder.Default
    private final int cost = 0;
}
