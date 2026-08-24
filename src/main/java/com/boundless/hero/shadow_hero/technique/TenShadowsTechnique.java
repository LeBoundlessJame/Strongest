package com.boundless.hero.shadow_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.ability.generic.SummonShikigamiAbility;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TenShadowsTechnique {
    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.identifier("megumi_punch"))
            .damage(18f)
            .attackDuration(8)
            .whiffSound(SoundRegistry.MISS_HIT)
            .impactSound(SoundRegistry.IMPACT_HEAVY_1)
            .build());

    public static final TechniqueAbility ROUNDHOUSE_KICK = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.identifier("megumi_roundhouse"))
            .animation(BoundlessAPI.identifier("roundhouse"))
            .damage(22f)
            .attackDuration(17)
            .impactTick(9)
            .animationSpeed(1.15f)
            .whiffSound(SoundRegistry.MISS_HIT)
            .impactSound(SoundRegistry.IMPACT_HEAVY_1)
            .build());

    public static final TechniqueAbility KURO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_KURO));
    public static final TechniqueAbility SHIRO = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.DIVINE_DOG_SHIRO));
    public static final TechniqueAbility GAMA = TechniqueAbilityRegistry.register(new SummonShikigamiAbility(EntityRegistry.GAMA));
}
