package com.boundless.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundRegistry {
    public static SoundEvent IMPACT_HEAVY_1 = registerSoundEvent(BoundlessAPI.id("impact_heavy_1"));
    public static SoundEvent MISS_HIT = registerSoundEvent(BoundlessAPI.id("miss_hit"));
    public static SoundEvent ROCK_CRUMBLING = registerSoundEvent(BoundlessAPI.id("rock_crumbling"));
    public static SoundEvent EARTH_IMPACT = registerSoundEvent(BoundlessAPI.id("earth_impact"));
    public static SoundEvent ENERGY_IMPACT_1 = registerSoundEvent(BoundlessAPI.id("energy_impact_1"));
    public static SoundEvent ENERGY_IMPACT_2 = registerSoundEvent(BoundlessAPI.id("energy_impact_2"));
    public static SoundEvent ENERGY_IMPACT_3 = registerSoundEvent(BoundlessAPI.id("energy_impact_3"));
    public static SoundEvent ENERGY_IMPACT_HEAVY = registerSoundEvent(BoundlessAPI.id("energy_impact_heavy"));
    public static SoundEvent CLAP_1 = registerSoundEvent(BoundlessAPI.id("clap_1"));

    public static SoundEvent HEAVY_CUT_1 = registerSoundEvent(BoundlessAPI.id("heavy_cut_1"));
    public static SoundEvent HEAVY_CUT_2 = registerSoundEvent(BoundlessAPI.id("heavy_cut_2"));
    public static SoundEvent HEAVY_CUT_3 = registerSoundEvent(BoundlessAPI.id("heavy_cut_3"));
    public static SoundEvent SLASH_1 = registerSoundEvent(BoundlessAPI.id("slash_1"));
    public static SoundEvent SLASH_2 = registerSoundEvent(BoundlessAPI.id("slash_2"));

    public static SoundEvent registerSoundEvent(Identifier identifier) {
        Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
        return SoundEvent.of(identifier);
    }

    public static void initialize() {}
}
