package com.boundless.hero.shadow_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

import static com.boundless.registry.DataComponentRegistry.registerComponent;

public class TenShadowsComponents {
    public static ComponentType<Boolean> SHIKIGAMI_ORDER_MODE = DataComponentRegistry.registerBoolean("shikigami_order_mode");
    public static final ComponentType<Map<String, NbtCompound>> SHIKIGAMI = registerComponent("shikigami", builder -> ComponentType.<Map<String, NbtCompound>>builder().codec(Codec.unboundedMap(Codec.STRING, NbtCompound.CODEC)));
    public static void initialize(){}
}
