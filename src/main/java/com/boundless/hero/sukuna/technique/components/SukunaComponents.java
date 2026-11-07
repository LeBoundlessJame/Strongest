package com.boundless.hero.sukuna.technique.components;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class SukunaComponents {
    public static ComponentType<Integer> PRIMARY_TARGET_ID = DataComponentRegistry.registerComponent("primary_target", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static void initialize(){}
}
