package com.boundless.hero.yuji.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentEnergyAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_ENERGY = TechniqueAbilityRegistry.register(new DivergentEnergyAbility());
}
