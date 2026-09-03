package com.boundless.hero.yuji.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class YujiComponents {
    public static ComponentType<Boolean> DIVERGENCE_ACTIVE = DataComponentRegistry.registerComponent("divergence_active", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static void initialize(){}
}
