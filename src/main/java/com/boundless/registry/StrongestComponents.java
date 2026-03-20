package com.boundless.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class StrongestComponents {
    public static ComponentType<Float> BLACK_FLASH_CHANCE = DataComponentRegistry.registerComponent("black_flash_chance", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Integer> CURSED_ENERGY = DataComponentRegistry.registerComponent("cursed_energy", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> SIMPLE_DOMAIN_HEALTH = DataComponentRegistry.registerComponent("simple_domain_health", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Boolean> SIMPLE_DOMAIN_ACTIVE = DataComponentRegistry.registerComponent("simple_domain_active", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static void initialize() {}
}
