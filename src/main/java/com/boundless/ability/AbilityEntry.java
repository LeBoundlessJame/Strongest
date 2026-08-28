package com.boundless.ability;

import com.boundless.loadouts.AbilityKey;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.function.Function;

public record AbilityEntry(AbilityKey key, Function<PlayerEntity, Identifier> resolver, Set<Identifier> possibleAbilities) {
    public Identifier getAbilityId(PlayerEntity player) {
        return resolver.apply(player);
    }
}
