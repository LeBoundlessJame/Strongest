package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.mechanics.AbilityManager;
import com.boundless.util.AttackUtils;

public class BasicAbilities {
    public static Ability COMBAT_MODE_TOGGLE = AbilityManager.ability(AttackUtils::toggleCombatMode, 2, BoundlessAPI.id("combat_mode_toggle"), null, "Combat Toggle");
}
