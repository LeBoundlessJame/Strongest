package com.boundless.registry;

import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.CombatAbilities;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class TechniqueAbilityRegistry {
    private static HashMap<Identifier, TechniqueAbility> ABILITIES = new HashMap<>();

    public static void initialize() {
        CombatAbilities.initialize();
    }

    public static <T extends TechniqueAbility> T register(T ability) {
        ABILITIES.put(ability.getId(), ability);
        return ability;
    }

    public static TechniqueAbility getAbilityFromID(Identifier abilityID) {
        return ABILITIES.get(abilityID);
    }
}
