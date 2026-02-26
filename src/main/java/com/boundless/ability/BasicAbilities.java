package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.util.AbilityUtils;
import com.boundless.util.AttackUtils;

public class BasicAbilities {
    public static Ability COMBAT_MODE_TOGGLE = AbilityUtils.ability(AttackUtils::toggleCombatMode, 2, BoundlessAPI.identifier("combat_mode_toggle"), null, "Combat Toggle");
}
