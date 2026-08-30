package com.boundless.hero.ratio_technique_hero;

import com.boundless.BoundlessAPI;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.combat.attack_modifiers.RatioModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioTechnique;
import com.boundless.hero.ratio_technique_hero.technique.RatioTechniqueAbilityResolver;
import com.boundless.hero.shadow_hero.technique.TenShadowsAttributes;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class RatioTechniqueHero extends Hero {
    public RatioTechniqueHero() {
        RatioComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, RatioTechnique.PUNCH)
                .ability(AbilityKey.ABILITY_ONE, RatioTechnique.RATIO)
                .ability(AbilityKey.USE, RatioTechniqueAbilityResolver::getRightClickAbility).build();

        this.heroData = HeroData.builder()
                .name("ratio_technique_hero")
                .defaultTechniqueLoadout(loadout)
                .attributes(TenShadowsAttributes.ATTRIBUTES)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("ratio_technique_hero"))
                .textureIdentifier(BoundlessAPI.textureID("nanami"))
                .tickHandler(Hero::onHeroTick)
                .tickHandler(RatioTechnique::ratioTick)
                .maxCursedEnergy(7000)
                .blackFlashChance(0.02f)
                .blackFlashDamageMultiplier(2.5f)
                .attackModifier(new BlackFlashModifier())
                .attackModifier(new RatioModifier())
                .build();

        this.registerHero();
    }
}
