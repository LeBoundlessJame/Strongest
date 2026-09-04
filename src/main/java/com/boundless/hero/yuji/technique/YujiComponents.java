package com.boundless.hero.yuji.technique;

import com.boundless.hero.yuji.technique.components.DivergentTarget;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.util.Uuids;

import java.util.List;
import java.util.UUID;

public class YujiComponents {
    public static ComponentType<List<DivergentTarget>> DIVERGENT_TARGETS = DataComponentRegistry.registerComponent("divergent_targets", builder -> ComponentType.<List<DivergentTarget>>builder().codec(Codec.list(DivergentTarget.CODEC)));
    public static ComponentType<Boolean> DIVERGENCE_ACTIVE = DataComponentRegistry.registerComponent("divergence_active", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static void initialize(){}
}
