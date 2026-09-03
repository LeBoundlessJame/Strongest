package com.boundless.hero.nanami;

import com.boundless.BoundlessAPI;
import com.boundless.combat.attack_modifiers.BlackFlashModifier;
import com.boundless.combat.attack_modifiers.RatioModifier;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.nanami.client.RatioHeroRenderer;
import com.boundless.hero.nanami.technique.RatioAttributes;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.hero.nanami.technique.RatioTechnique;
import com.boundless.hero.nanami.technique.RatioTechniqueAbilityResolver;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;

public class Nanami extends Hero {
    public Nanami() {
        RatioComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, RatioTechnique.PUNCH)
                .ability(AbilityKey.ABILITY_ONE, RatioTechnique.RATIO)
                .ability(AbilityKey.ABILITY_TWO, RatioTechnique.COLLAPSE)
                .ability(AbilityKey.ABILITY_THREE, RatioTechniqueAbilityResolver::getOvertimeAbility)
                .ability(AbilityKey.USE, RatioTechniqueAbilityResolver::getRightClickAbility).build();

        this.heroData = HeroData.builder()
                .name("nanami")
                .defaultTechniqueLoadout(loadout)
                .attributes(RatioAttributes.ATTRIBUTES)
                .armorRenderer(RatioHeroRenderer::new)
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
