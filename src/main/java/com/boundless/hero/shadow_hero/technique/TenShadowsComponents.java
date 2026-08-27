package com.boundless.hero.shadow_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

import static com.boundless.registry.DataComponentRegistry.registerComponent;

public class TenShadowsComponents {
    public static ComponentType<Boolean> SHIKIGAMI_ORDER_MENU = DataComponentRegistry.registerBoolean("shikigami_order_menu");
    public static ComponentType<String> CURRENT_ORDER_SEQUENCE = DataComponentRegistry.registerComponent("current_order_sequence", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static final ComponentType<Map<String, NbtCompound>> SHIKIGAMI = registerComponent("shikigami", builder -> ComponentType.<Map<String, NbtCompound>>builder().codec(Codec.unboundedMap(Codec.STRING, NbtCompound.CODEC)));
    public static void initialize(){}
}
