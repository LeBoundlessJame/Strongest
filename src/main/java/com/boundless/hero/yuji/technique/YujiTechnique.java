package com.boundless.hero.yuji.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentFistAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_FIST = TechniqueAbilityRegistry.register(new DivergentFistAbility());
}
