package com.boundless.hero.sukuna.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.CombatAbilities;
import com.boundless.hero.sukuna.technique.abilities.*;
import com.boundless.registry.StrongestComponents;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class SukunaTechnique {
    public static final TechniqueAbility DISMANTLE = TechniqueAbilityRegistry.register(new DismantleAbility());
    public static final TechniqueAbility CLEAVE = TechniqueAbilityRegistry.register(new CleaveAbility());
    public static final TechniqueAbility OPEN = TechniqueAbilityRegistry.register(new OpenAbility());
    public static final TechniqueAbility MALEVOLENT_SHRINE = TechniqueAbilityRegistry.register(new MalevolentShrineAbility());
}
