package com.boundless.hero.nobara;

import com.boundless.BoundlessAPI;
import com.boundless.combat.CombatAbilities;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.combat.attack_modifiers.ResonanceTrackingModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.nobara.technique.StrawdollTechnique;
import com.boundless.hero.nobara.technique.components.StrawdollComponents;
import com.boundless.hero.sukuna.technique.SukunaTechnique;
import com.boundless.hero.sukuna.technique.components.SukunaComponents;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Nobara extends Hero {
    public Nobara() {
        StrawdollComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ABILITY_ONE, StrawdollTechnique.HAIRPIN)
                .ability(AbilityKey.ABILITY_TWO, StrawdollTechnique.RESONANCE)
                .ability(AbilityKey.ABILITY_THREE, StrawdollTechnique.THROW_NAIL)
                .ability(AbilityKey.ABILITY_FOUR, StrawdollTechnique.TRIPLE_NAIL_THROW)
                .ability(AbilityKey.ATTACK, CombatAbilities.PUNCH)
                .ability(AbilityKey.USE, CombatAbilities.ROUNDHOUSE_KICK)
                .build();

        this.heroData = HeroData.builder()
                .name("nobara")
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
                .attackModifier(new ResonanceTrackingModifier())
                .build();

        this.registerHero();
    }
}
