package com.boundless.loadouts;

import com.boundless.ability.AbilityEntry;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Function;

public class TechniqueLoadout {
    private final List<AbilityEntry> abilities;
    private final Map<AbilityKey, AbilityEntry> keybindAbilities;

    private TechniqueLoadout(List<AbilityEntry> abilities) {
        this.abilities = List.copyOf(abilities);

        Map<AbilityKey, AbilityEntry> keybindAbilities = new HashMap<>();

        for (AbilityEntry entry: this.abilities) {
            if (entry.key() == null) continue;

            keybindAbilities.put(entry.key(), entry);
        }

        this.keybindAbilities = Map.copyOf(keybindAbilities);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<AbilityEntry> getAbilities() {
        return abilities;
    }

    public AbilityEntry getKeybindAbility(AbilityKey key) {
        return keybindAbilities.get(key);
    }

    public Identifier getAbilityId(AbilityKey key, PlayerEntity player) {
        AbilityEntry entry = keybindAbilities.get(key);

        if (entry == null) return TechniqueAbilityRegistry.EMPTY.getAbilityId();

        return entry.getAbilityId(player);
    }

    public static class Builder {
        private final List<AbilityEntry> abilities = new ArrayList<>();

        public Builder ability(AbilityKey key, TechniqueAbility ability) {
            return ability(key, playerEntity -> ability.getAbilityId(), ability.getAbilityId());
        }

        public Builder ability(AbilityKey key, Function<PlayerEntity, Identifier> resolver, Identifier... possibleAbilities) {
            abilities.add(new AbilityEntry(key, resolver, Set.of(possibleAbilities)));
            return this;
        }

        public Builder ability(TechniqueAbility ability) {
            return ability(null, playerEntity -> ability.getAbilityId(), ability.getAbilityId());
        }

        public Builder ability(Function<PlayerEntity, Identifier> resolver, Identifier... possibleAbilities) {
            return ability(null, resolver, possibleAbilities);
        }

        public TechniqueLoadout build() {
            return new TechniqueLoadout(abilities);
        }
    }

    public TechniqueLoadoutComponent asComponent() {
        Set<Identifier> allAbilities = new HashSet<>();
        for (AbilityEntry entry: abilities) {
            allAbilities.addAll(entry.possibleAbilities());
        }
        return new TechniqueLoadoutComponent(Set.copyOf(allAbilities));
    }
}
