package com.boundless.loadouts;

import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TechniqueLoadout {
    private final Map<AbilityKey, Function<PlayerEntity, Identifier>> abilities;
    private final Map<AbilityKey, Set<Identifier>> allPossibleAbilities;

    private TechniqueLoadout(Map<AbilityKey, Function<PlayerEntity, Identifier>> abilities, Map<AbilityKey, Set<Identifier>> allPossibleAbilities) {
        this.abilities = abilities;
        this.allPossibleAbilities = allPossibleAbilities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Identifier getAbilityId(AbilityKey key, PlayerEntity player) {
        Function<PlayerEntity, Identifier> resolver = abilities.get(key);

        if (resolver == null) return TechniqueAbilityRegistry.EMPTY.getAbilityId();

        return resolver.apply(player);
    }

    public static class Builder {
        private final Map<AbilityKey, Function<PlayerEntity, Identifier>> abilities = new HashMap<>();
        private final Map<AbilityKey, Set<Identifier>> allPossibleAbilities = new HashMap<>();

        public Builder ability(AbilityKey key, TechniqueAbility ability) {
            abilities.put(key, player -> ability.getAbilityId());
            allPossibleAbilities.computeIfAbsent(key, idSet -> new HashSet<>()).add(ability.getAbilityId());
            return this;
        }

        public Builder ability(AbilityKey key, Function<PlayerEntity, Identifier> resolver, Identifier... possibleAbilities) {
            abilities.put(key, resolver);
            allPossibleAbilities.computeIfAbsent(key, idSet -> new HashSet<>()).addAll(Set.of(possibleAbilities));
            return this;
        }

        public TechniqueLoadout build() {
            return new TechniqueLoadout(Map.copyOf(abilities), Map.copyOf(allPossibleAbilities));
        }
    }

    public TechniqueLoadoutComponent asComponent() {
        return new TechniqueLoadoutComponent(allPossibleAbilities);
    }
}
