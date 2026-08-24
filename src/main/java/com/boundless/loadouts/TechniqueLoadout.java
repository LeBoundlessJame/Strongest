package com.boundless.loadouts;

import com.boundless.ability.Ability;
import com.boundless.ability.TechniqueAbility;
import lombok.Builder;
import lombok.Singular;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class TechniqueLoadout {
    private final Map<AbilityKey, Identifier> abilities;

    private TechniqueLoadout(Map<AbilityKey, Identifier> abilities) {
        this.abilities = abilities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<AbilityKey, Identifier> abilities = new HashMap<>();

        public Builder ability(AbilityKey key, TechniqueAbility ability) {
            abilities.put(key, ability.getAbilityId());
            return this;
        }

        public TechniqueLoadout build() {
            return new TechniqueLoadout(Map.copyOf(abilities));
        }
    }

    public TechniqueLoadoutComponent asComponent() {
        return new TechniqueLoadoutComponent(abilities);
    }
}
