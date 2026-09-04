package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class CombatAbilities {
    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility());
    public static final TechniqueAbility ROUNDHOUSE_KICK = TechniqueAbilityRegistry.register(new KickAbility());
    public static void initialize() {}
}
