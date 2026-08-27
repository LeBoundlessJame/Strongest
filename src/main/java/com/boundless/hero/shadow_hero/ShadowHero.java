package com.boundless.hero.shadow_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.black_sparks_hero.BrawlerHUD;
import com.boundless.hero.shadow_hero.technique.TenShadowsAttributes;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class ShadowHero extends Hero {
    public ShadowHero() {
        TenShadowsComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, TenShadowsTechnique.PUNCH)
                .ability(AbilityKey.USE, TenShadowsTechnique::getRightClickAbility, TenShadowsTechnique.GAMA_GRAPPLE.getAbilityId(), TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId())
                .ability(AbilityKey.ABILITY_TWO, TenShadowsTechnique.SHIRO)
                .ability(AbilityKey.ABILITY_THREE, TenShadowsTechnique.GAMA)
                .ability(AbilityKey.ABILITY_ONE, TenShadowsTechnique.KURO)
                .ability(AbilityKey.ABILITY_FOUR, TenShadowsTechnique.TOGGLE_SHIKIGAMI_ORDERS_MENU)
                .build();

        this.heroData = HeroData.builder()
                .name("shadow_hero")
                .defaultTechniqueLoadout(loadout)
                .attributes(TenShadowsAttributes.ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("shadow_hero"))
                .textureIdentifier(BoundlessAPI.textureID("shadow_hero"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(5000)
                .build();
        this.registerHero();
    }
}

