package com.boundless.hero.todo.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.hero.todo.technique.abilities.BoogieWoogieAbility;
import com.boundless.hero.todo.technique.abilities.SelectTargetAbility;
import com.boundless.hero.todo.technique.abilities.SuplexAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class TodoTechnique {
    public static final TechniqueAbility BOOGIE_WOOGIE = TechniqueAbilityRegistry.register(new BoogieWoogieAbility());
    public static final TechniqueAbility SUPLEX = TechniqueAbilityRegistry.register(new SuplexAbility());
    public static final TechniqueAbility SELECT_PRIMARY_TARGET = TechniqueAbilityRegistry.register(new SelectTargetAbility(SelectTargetAbility.Target.PRIMARY));
    public static final TechniqueAbility SELECT_SECONDARY_TARGET = TechniqueAbilityRegistry.register(new SelectTargetAbility(SelectTargetAbility.Target.SECONDARY));
}
