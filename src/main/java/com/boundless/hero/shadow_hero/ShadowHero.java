package com.boundless.hero.shadow_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.shadow_hero.technique.TenShadowsAbilityResolver;
import com.boundless.hero.shadow_hero.technique.TenShadowsAttributes;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class ShadowHero extends Hero {
    public ShadowHero() {
        TenShadowsComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ABILITY_FOUR, TenShadowsTechnique.TOGGLE_SHIKIGAMI_ORDERS_MENU)
                .ability(AbilityKey.ATTACK, TenShadowsAbilityResolver::getLeftClickAbility, TenShadowsTechnique.SHIKIGAMI_ORDER_LEFT.getAbilityId(), TenShadowsTechnique.PUNCH.getAbilityId())
                .ability(AbilityKey.USE, TenShadowsAbilityResolver::getRightClickAbility, TenShadowsTechnique.SHIKIGAMI_ORDER_RIGHT.getAbilityId(), TenShadowsTechnique.GAMA_GRAPPLE.getAbilityId(), TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId())
                .ability(AbilityKey.ABILITY_ONE, TenShadowsAbilityResolver::getAbilityOne)
                .ability(AbilityKey.ABILITY_TWO, TenShadowsAbilityResolver::getAbilityTwo)
                .ability(AbilityKey.ABILITY_THREE, TenShadowsAbilityResolver::getAbilityThree)
                .ability(TenShadowsAbilityResolver::getGamaPull)
                .build();

        this.heroData = HeroData.builder()
                .name("shadow_hero")
                .defaultTechniqueLoadout(loadout)
                .attributes(TenShadowsAttributes.ATTRIBUTES)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("shadow_hero"))
                .textureIdentifier(BoundlessAPI.textureID("shadow_hero"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(5000)
                .blackFlashDamageMultiplier(2.5f)
                .blackFlashChance(0.01f)
                .build();
        this.registerHero();
    }
}

