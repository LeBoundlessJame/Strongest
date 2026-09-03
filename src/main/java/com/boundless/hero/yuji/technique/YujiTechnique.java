package com.boundless.hero.yuji.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentEnergyAbility;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.util.math.Vec3d;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_ENERGY = TechniqueAbilityRegistry.register(new DivergentEnergyAbility());

    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility()
            .setAbilityId(BoundlessAPI.id("megumi_punch"))
            .setDamage(22)
            .setAttackDuration(8));

    public static final TechniqueAbility ROUNDHOUSE_KICK = TechniqueAbilityRegistry.register(new PunchAbility()
            .setAbilityId(BoundlessAPI.id("yuji_roundhouse"))
            .setAnimation(BoundlessAPI.id("roundhouse"))
            .setDamage(44)
            .setAttackDuration(17)
            .setImpactTick(9)
            .setAnimationSpeed(1.15f)
            .setKnockback(new Vec3d(1.2, 0.6, 1.2)));
}
