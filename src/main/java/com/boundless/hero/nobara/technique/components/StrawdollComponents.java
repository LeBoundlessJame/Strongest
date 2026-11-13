package com.boundless.hero.nobara.technique.components;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

import java.util.Map;
import java.util.UUID;

public class StrawdollComponents {
    public static ComponentType<ResonanceTracker> RESONANCE_TRACKER = DataComponentRegistry.registerComponent("sukuna_fingers_consumed", builder -> ComponentType.<ResonanceTracker>builder().codec(ResonanceTracker.CODEC));
    public static void initialize(){}
}
