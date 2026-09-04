package com.boundless.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class StrongestComponents {
    public static ComponentType<Float> BLACK_FLASH_CHANCE = DataComponentRegistry.registerComponent("black_flash_chance", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Float> BLACK_FLASH_DAMAGE_MULTIPLIER = DataComponentRegistry.registerComponent("black_flash_damage_multiplier", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Float> MELEE_STRENGTH = DataComponentRegistry.registerComponent("melee_strength", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));

    public static void initialize() {}
}
