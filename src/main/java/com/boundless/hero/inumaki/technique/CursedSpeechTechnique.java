package com.boundless.hero.inumaki.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.sukuna.technique.abilities.DismantleAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class CursedSpeechTechnique {
    public static final TechniqueAbility DONT_MOVE = TechniqueAbilityRegistry.register(new DontMoveAbility());
    public static final TechniqueAbility BLAST_AWAY = TechniqueAbilityRegistry.register(new BlastAwayAbility());
    public static final TechniqueAbility EXPLODE = TechniqueAbilityRegistry.register(new ExplodeAbility());
}
