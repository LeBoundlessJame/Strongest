package com.boundless.hero.ratio_technique_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.shadow_hero.technique.TenShadowsAttributes;
import com.boundless.loadouts.TechniqueLoadout;

public class RatioTechniqueHero extends Hero {
    public RatioTechniqueHero() {
        TechniqueLoadout loadout = TechniqueLoadout.builder().build();

        this.heroData = HeroData.builder()
                .name("ratio_technique_hero")
                .defaultTechniqueLoadout(loadout)
                .attributes(TenShadowsAttributes.ATTRIBUTES)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("ratio_technique_hero"))
                .textureIdentifier(BoundlessAPI.textureID("nanami"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(7000)
                .build();
        this.registerHero();
    }
}
