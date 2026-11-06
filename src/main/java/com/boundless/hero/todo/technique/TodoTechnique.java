package com.boundless.hero.todo.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.hero.todo.technique.abilities.BoogieWoogieAbility;
import com.boundless.hero.todo.technique.abilities.SuplexAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TodoTechnique {
    public static final TechniqueAbility BOOGIE_WOOGIE = TechniqueAbilityRegistry.register(new BoogieWoogieAbility());
    public static final TechniqueAbility SUPLEX = TechniqueAbilityRegistry.register(new SuplexAbility());
}
