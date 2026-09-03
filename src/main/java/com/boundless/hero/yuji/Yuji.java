package com.boundless.hero.yuji;

import com.boundless.BoundlessAPI;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.nanami.technique.RatioTechnique;
import com.boundless.hero.nanami.technique.RatioTechniqueAbilityResolver;
import com.boundless.hero.yuji.technique.YujiAttributes;
import com.boundless.hero.yuji.technique.components.YujiComponents;
import com.boundless.hero.yuji.technique.YujiTechnique;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Yuji extends Hero {
    public Yuji() {
        YujiComponents.initialize();

        // Todo: Might make roundhouse generic, and then make it calculated on player's melee stat
        // For now, will use megumi's roundhouse until I get things setup
        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, YujiTechnique.PUNCH)
                .ability(AbilityKey.ABILITY_ONE, YujiTechnique.DIVERGENT_ENERGY)
                .ability(AbilityKey.USE, YujiTechnique.ROUNDHOUSE_KICK).build();

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
                .attackModifier(new BlackFlashModifier())
                .build();

        this.registerHero();
    }
}
