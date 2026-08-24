package com.boundless.loadouts;

import com.boundless.ability.Ability;
import com.boundless.ability.TechniqueAbility;
import lombok.Builder;
import lombok.Singular;

import java.util.Map;

@Builder
public class TechniqueLoadout {
    @Singular
    private final Map<AbilityKey, TechniqueAbility> abilities;
}
