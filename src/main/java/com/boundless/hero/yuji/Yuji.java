package com.boundless.hero.yuji;

import com.boundless.BoundlessAPI;
import com.boundless.combat.CombatAbilities;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.combat.attack_modifiers.DivergentModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.yuji.technique.YujiAttributes;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.hero.yuji.technique.YujiTechnique;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Yuji extends Hero {
    public Yuji() {
        YujiComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, CombatAbilities.PUNCH)
                .ability(AbilityKey.ABILITY_ONE, YujiTechnique.DIVERGENT_FIST)
                .ability(AbilityKey.USE, CombatAbilities.ROUNDHOUSE_KICK).build();

        this.heroData = HeroData.builder()
                .name("yuji")
                .defaultTechniqueLoadout(loadout)
                .attributes(YujiAttributes.ATTRIBUTES)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("yuji"))
                .textureIdentifier(BoundlessAPI.textureID("yuji"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(5000)
                .blackFlashChance(0.08f)
                .blackFlashDamageMultiplier(1.9f)
                .meleeStrength(22)
                .attackModifier(new BlackFlashModifier())
                .attackModifier(new DivergentModifier())
                .build();

        this.registerHero();
    }
}
