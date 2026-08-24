package com.boundless.hero.shadow_hero.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.SummonShikigamiAbility;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TenShadowsTechnique {
    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility());
    public static final TechniqueAbility KURO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_KURO));
    public static final TechniqueAbility SHIRO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_SHIRO));
    public static final TechniqueAbility GAMA = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.GAMA));
}
