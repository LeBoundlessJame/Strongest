package com.boundless.hero.shadow_hero.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TenShadowsTechnique {
    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility());
}
