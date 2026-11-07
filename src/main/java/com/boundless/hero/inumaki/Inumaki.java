package com.boundless.hero.inumaki;

import com.boundless.BoundlessAPI;
import com.boundless.combat.CombatAbilities;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.inumaki.technique.CursedSpeechTechnique;
import com.boundless.hero.inumaki.technique.DontMoveAbility;
import com.boundless.hero.sukuna.technique.SukunaTechnique;
import com.boundless.hero.sukuna.technique.components.SukunaComponents;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Inumaki extends Hero {
    public Inumaki() {
        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ABILITY_ONE, CursedSpeechTechnique.DONT_MOVE)
                .ability(AbilityKey.ABILITY_TWO, CursedSpeechTechnique.BLAST_AWAY)
                .ability(AbilityKey.ABILITY_THREE, CursedSpeechTechnique.EXPLODE)
                .ability(AbilityKey.ATTACK, CombatAbilities.PUNCH)
                .ability(AbilityKey.USE, CombatAbilities.ROUNDHOUSE_KICK)
                .build();

        this.heroData = HeroData.builder()
                .name("inumaki")
                .defaultTechniqueLoadout(loadout)
                .tickHandler(Hero::heroSprintHandler)
                .textureIdentifier(BoundlessAPI.textureID("inumaki"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(8000)
                .blackFlashChance(0.04f)
                .blackFlashDamageMultiplier(2.0f)
                .meleeStrength(20)
                .bonus_health(480)
                .attackModifier(new BlackFlashModifier())
                .build();

        this.registerHero();
    }
}