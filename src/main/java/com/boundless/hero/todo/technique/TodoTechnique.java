package com.boundless.hero.todo.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.hero.todo.technique.abilities.BoogieWoogieAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TodoTechnique {
    public static final TechniqueAbility BOOGIE_WOOGIE = TechniqueAbilityRegistry.register(new BoogieWoogieAbility());
}
