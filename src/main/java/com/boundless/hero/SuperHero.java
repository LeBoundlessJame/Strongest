package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.ability.reusable_abilities.flight.FlightAbilities;
import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;

public class SuperHero extends Hero {
    public SuperHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", MeleeCombatAbilities.JAB)
                .ability("key.jump", FlightAbilities.BOOST)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);

        this.heroData = HeroData
                .builder()
                .name("super_hero")
                .displayName("Super Hero")
                .textureIdentifier(BoundlessAPI.textureID("super_hero"))
                .tickHandler(FlightAbility::flightTick)
                .defaultAbilityLoadout(ABILITY_LOADOUTS.get("LOADOUT_1"))
                .build();

        this.registerHero();
    }
}