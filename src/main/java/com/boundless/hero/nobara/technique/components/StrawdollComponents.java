package com.boundless.hero.nobara.technique.components;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.util.Uuids;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StrawdollComponents {
    public static ComponentType<Map<UUID, ResonanceData>> RESONANCE_TRACKER = DataComponentRegistry.registerComponent("resonance_tracker", builder -> ComponentType.<Map<UUID, ResonanceData>>builder().codec(Codec.unboundedMap(Uuids.CODEC, ResonanceData.CODEC)));
    public static ComponentType<Map<UUID, List<CursedNailData>>> HAIRPIN_TARGETS = DataComponentRegistry.registerComponent("resonance_tracker", builder -> ComponentType.<Map<UUID, List<CursedNailData>>>builder().codec(Codec.unboundedMap(Uuids.CODEC, Codec.list(CursedNailData.CODEC))));
    public static void initialize(){}
}
