package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.ability.reusable_abilities.flight.FlightAbilities;
import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.client.KeyInputHandler;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.component.ComponentType;

public class SuperHero extends Hero {
    public static ComponentType<Integer> FLIGHT_TICKS = DataComponentRegistry.registerInt("flight_ticks");
    public static ComponentType<Long> FLIGHT_BEGIN_TIMESTAMP = DataComponentRegistry.registerLong("flight_begin_timestamp");
    public static ComponentType<String> FLIGHT_DIRECTION = DataComponentRegistry.registerString("flight_direction");
    public static ComponentType<Boolean> BOOSTING = DataComponentRegistry.registerBoolean("boosting");
    public static ComponentType<Integer> BOOST_TICKS = DataComponentRegistry.registerInt("boost_ticks");
    public static ComponentType<Long> BOOST_NEXT_USABLE = DataComponentRegistry.registerLong("boost_next_usable");

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