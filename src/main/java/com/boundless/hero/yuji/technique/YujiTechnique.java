package com.boundless.hero.yuji.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentFistAbility;
import com.boundless.hero.yuji.technique.abilities.ManjiKickAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_FIST = TechniqueAbilityRegistry.register(new DivergentFistAbility());
    public static final TechniqueAbility MANJI_KICK = TechniqueAbilityRegistry.register(new ManjiKickAbility());
}
