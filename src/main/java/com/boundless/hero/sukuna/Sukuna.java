package com.boundless.hero.sukuna;

import com.boundless.BoundlessAPI;
import com.boundless.combat.CombatAbilities;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.sukuna.technique.SukunaTechnique;
import com.boundless.hero.sukuna.technique.components.SukunaComponents;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Sukuna extends Hero {
    public Sukuna() {
        SukunaComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ABILITY_ONE, SukunaTechnique.DISMANTLE)
                .ability(AbilityKey.ABILITY_TWO, SukunaTechnique.CLEAVE)
                .ability(AbilityKey.ABILITY_THREE, SukunaTechnique.OPEN)
                .ability(AbilityKey.ABILITY_FOUR, SukunaTechnique.MALEVOLENT_SHRINE)
                .ability(AbilityKey.ATTACK, CombatAbilities.PUNCH)
                .ability(AbilityKey.USE, CombatAbilities.ROUNDHOUSE_KICK)
                .build();

        this.heroData = HeroData.builder()
                .name("sukuna")
                .defaultTechniqueLoadout(loadout)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("yuji"))
                .textureIdentifier(BoundlessAPI.textureID("sukuna"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(15000)
                .blackFlashChance(0.04f)
                .blackFlashDamageMultiplier(2.0f)
                .meleeStrength(22)
                .bonus_health(680)
                .attackModifier(new BlackFlashModifier())
                .build();

        this.registerHero();
    }
}
