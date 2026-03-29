package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.effect.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class StatusEffectRegistry {
    public static final RegistryEntry<StatusEffect> INVULNERABILITY_EFFECT = registerGenericEffect("invulnerability");
    public static final RegistryEntry<StatusEffect> IMPACT_FRAME_EFFECT = registerGenericEffect("impact_frame_effect");
    public static final RegistryEntry<StatusEffect> CLAP_IMPACT_FRAME_EFFECT = registerGenericEffect("clap_impact_frame_effect");
    public static final RegistryEntry<StatusEffect> BONE_BREAK_EFFECT = registerGenericEffect("bone_break_effect");
    public static final RegistryEntry<StatusEffect> CINEMATIC_BARS = registerGenericEffect("cinematic_bars_effect");
    public static final RegistryEntry<StatusEffect> SHRINE_EFFECT = registerGenericEffect("shrine_effect");
    public static final RegistryEntry<StatusEffect> GRAYSCALE = registerGenericEffect("grayscale_effect");
    public static final RegistryEntry<StatusEffect> LIMITED_SPEED = registerGenericEffect("limited_speed");
    public static final RegistryEntry<StatusEffect> GUARD_BREAK = registerGenericEffect("guard_break");
    public static final RegistryEntry<StatusEffect> STUN = registerGenericEffect("stun", new StunEffect(StatusEffectCategory.HARMFUL, 0xffffff));
    public static final RegistryEntry<StatusEffect> ZONE = registerGenericEffect("zone");

    private static RegistryEntry<StatusEffect> registerGenericEffect(String name) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BoundlessAPI.identifier(name), new GenericStatusEffect(StatusEffectCategory.NEUTRAL, 0xffffff));
    }

    private static RegistryEntry<StatusEffect> registerGenericEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BoundlessAPI.identifier(name), statusEffect);
    }

    public static void initialize() {}
}
