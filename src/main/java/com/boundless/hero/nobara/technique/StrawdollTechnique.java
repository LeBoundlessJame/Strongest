package com.boundless.hero.nobara.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.nobara.technique.abilities.HairpinAbility;
import com.boundless.hero.nobara.technique.abilities.ResonanceAbility;
import com.boundless.hero.nobara.technique.abilities.NailThrowAbility;
import com.boundless.hero.nobara.technique.abilities.TripleNailThrowAbility;
import com.boundless.registry.TechniqueAbilityRegistry;

public class StrawdollTechnique {
    public static final TechniqueAbility HAIRPIN = TechniqueAbilityRegistry.register(new HairpinAbility());
    public static final TechniqueAbility RESONANCE = TechniqueAbilityRegistry.register(new ResonanceAbility());
    public static final TechniqueAbility THROW_NAIL = TechniqueAbilityRegistry.register(new NailThrowAbility());
    public static final TechniqueAbility TRIPLE_NAIL_THROW = TechniqueAbilityRegistry.register(new TripleNailThrowAbility());
}