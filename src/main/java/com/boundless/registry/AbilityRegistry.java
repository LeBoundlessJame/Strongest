package com.boundless.registry;

import com.boundless.ability.Ability;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class AbilityRegistry {
    public static HashMap<Identifier, Ability> ABILITIES = new HashMap<>();
    public static void initialize() {}

    public static Ability getAbilityFromID(Identifier abilityID) {
        return ABILITIES.getOrDefault(abilityID, null);
    }
}
