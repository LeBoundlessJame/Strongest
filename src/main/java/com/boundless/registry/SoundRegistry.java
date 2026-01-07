package com.boundless.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static SoundEvent IMPACT_HEAVY_1 = registerSoundEvent(BoundlessAPI.identifier("impact_heavy_1"));
    public static SoundEvent MISS_HIT = registerSoundEvent(BoundlessAPI.identifier("miss_hit"));
    public static SoundEvent ROCK_CRUMBLING = registerSoundEvent(BoundlessAPI.identifier("rock_crumbling"));
    public static SoundEvent EARTH_IMPACT = registerSoundEvent(BoundlessAPI.identifier("earth_impact"));
    public static SoundEvent ENERGY_IMPACT_1 = registerSoundEvent(BoundlessAPI.identifier("energy_impact_1"));
    public static SoundEvent ENERGY_IMPACT_2 = registerSoundEvent(BoundlessAPI.identifier("energy_impact_2"));
    public static SoundEvent ENERGY_IMPACT_3 = registerSoundEvent(BoundlessAPI.identifier("energy_impact_3"));
    public static SoundEvent ENERGY_IMPACT_HEAVY = registerSoundEvent(BoundlessAPI.identifier("energy_impact_heavy"));
    public static SoundEvent CLAP_1 = registerSoundEvent(BoundlessAPI.identifier("clap_1"));

    public static SoundEvent registerSoundEvent(Identifier identifier) {
        Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
        return SoundEvent.of(identifier);
    }

    public static void initialize() {}
}
