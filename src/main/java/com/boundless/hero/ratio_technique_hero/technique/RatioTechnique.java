package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.AttemptSkillcheckAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.RatioAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;

public class RatioTechnique {
    public static final TechniqueAbility RATIO = TechniqueAbilityRegistry.register(new RatioAbility());
    public static final TechniqueAbility ATTEMPT_SKILLCHECK = TechniqueAbilityRegistry.register(new AttemptSkillcheckAbility());

    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.id("nanami_punch"))
            .damage(22)
            .attackDuration(8)
            .whiffSound(SoundRegistry.MISS_HIT)
            .impactSound(SoundRegistry.IMPACT_HEAVY_1)
            .onHitEvent(((playerEntity, livingEntity) -> {
                RATIO.use(playerEntity);
            }))
            .build());
}
