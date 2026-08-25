package com.boundless.registry;

import com.boundless.ability.Ability;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.EmptyAbility;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class TechniqueAbilityRegistry {
    private static HashMap<Identifier, TechniqueAbility> ABILITIES = new HashMap<>();
    // Todo: remove after refactor
    public static final TechniqueAbility EMPTY = TechniqueAbilityRegistry.register(new EmptyAbility());

    public static void initialize() {}

    public static <T extends TechniqueAbility> T register(T ability) {
        ABILITIES.put(ability.getAbilityId(), ability);
        return ability;
    }

    public static TechniqueAbility getAbilityFromID(Identifier abilityID) {
        return ABILITIES.get(abilityID);
    }
}
