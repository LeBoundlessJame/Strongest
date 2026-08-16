package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.ability.components.KeybindHoldData;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.UnaryOperator;

// Todo: Clean this up!!
public class DataComponentRegistry {
    public static final ComponentType<Map<String, Identifier>> ABILITY_LOADOUT = registerComponent("ability_loadout", builder -> ComponentType.<Map<String, Identifier>>builder().codec(Codec.unboundedMap(Codec.STRING, Identifier.CODEC)));
    public static final ComponentType<Map<Identifier, Long>> COOLDOWN_DATA = registerComponent("cooldown_data", builder -> ComponentType.<Map<Identifier, Long>>builder().codec(Codec.unboundedMap(Identifier.CODEC, Codec.LONG)));
    public static ComponentType<Long> ATTACK_START = registerComponent("attack_start", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> ATTACK_END = registerComponent("attack_end", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> ROLLING_END = registerComponent("rolling_end", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Integer> SPRINT_TICKS = registerComponent("sprint_ticks", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> ATTACK_COUNT = registerComponent("attack_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Float> ENERGY_METER = DataComponentRegistry.registerComponent("energy_meter", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Integer> BOUND_CAMERA_ID = DataComponentRegistry.registerComponent("bound_camera_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Boolean> COMBAT_MODE_ENABLED = DataComponentRegistry.registerComponent("combat_mode_enabled", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));

    public static final ComponentType<Map<String, KeybindHoldData>> HELD_KEYBIND = registerComponent("held_keybind", builder -> ComponentType.<Map<String, KeybindHoldData>>builder().codec(Codec.unboundedMap(Codec.STRING, CodecRegistry.KEYBIND_HOLD_CODEC)));

    public static <T> ComponentType<T> registerComponent(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), builder.apply(ComponentType.builder()).build());
    }

    public static void initialize() {}
}
