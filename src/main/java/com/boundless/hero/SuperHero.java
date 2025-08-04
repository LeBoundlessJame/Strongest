package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.ability.reusable_abilities.flight.FlightRendering;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class SuperHero extends Hero {
    public static ComponentType<Integer> FLIGHT_TICKS = DataComponentRegistry.registerComponent("flight_ticks", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Long> FLIGHT_BEGIN_TIMESTAMP = DataComponentRegistry.registerComponent("flight_begin_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<String> FLIGHT_DIRECTION = DataComponentRegistry.registerComponent("flight_direction", builder -> ComponentType.<String>builder().codec(Codec.STRING));


    public SuperHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", MeleeCombatAbilities.JAB)
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