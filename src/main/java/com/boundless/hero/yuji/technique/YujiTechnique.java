package com.boundless.hero.yuji.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentEnergyAbility;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.util.math.Vec3d;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_ENERGY = TechniqueAbilityRegistry.register(new DivergentEnergyAbility());
}
