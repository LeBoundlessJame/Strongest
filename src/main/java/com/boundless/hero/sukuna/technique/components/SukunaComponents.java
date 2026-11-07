package com.boundless.hero.sukuna.technique.components;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class SukunaComponents {
    public static ComponentType<Integer> SUKUNA_FINGERS_CONSUMED = DataComponentRegistry.registerComponent("sukuna_fingers_consumed", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static void initialize(){}
}
