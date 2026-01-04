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

public class DataComponentRegistry {
    public static ComponentType<Boolean> VANILLA_MECHANICS = registerComponent("vanilla_mechanics", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static final ComponentType<Map<String, Identifier>> ABILITY_LOADOUT = registerComponent("ability_loadout", builder -> ComponentType.<Map<String, Identifier>>builder().codec(Codec.unboundedMap(Codec.STRING, Identifier.CODEC)));
    public static final ComponentType<Map<Identifier, Long>> COOLDOWN_DATA = registerComponent("cooldown_data", builder -> ComponentType.<Map<Identifier, Long>>builder().codec(Codec.unboundedMap(Identifier.CODEC, Codec.LONG)));
    public static ComponentType<Long> ATTACK_START = registerComponent("attack_start", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> ATTACK_END = registerComponent("attack_end", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> ROLLING_END = registerComponent("rolling_end", builder -> ComponentType.<Long>builder().codec(Codec.LONG));

    public static ComponentType<Integer> SPRINT_TICKS = registerComponent("sprint_ticks", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> ATTACK_COUNT = registerComponent("attack_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public static final ComponentType<Map<String, KeybindHoldData>> HELD_KEYBIND = registerComponent("held_keybind", builder -> ComponentType.<Map<String, KeybindHoldData>>builder().codec(Codec.unboundedMap(Codec.STRING, CodecRegistry.KEYBIND_HOLD_CODEC)));

    public static <T> ComponentType<T> registerComponent(String name, UnaryOperator<ComponentType.Builder<T>> builder) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), builder.apply(ComponentType.builder()).build());
    }

    public static <T> ComponentType<Boolean> registerBoolean(String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), ComponentType.<Boolean>builder().codec(Codec.BOOL).build());
    }

    public static <T> ComponentType<Long> registerLong(String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), ComponentType.<Long>builder().codec(Codec.LONG).build());
    }

    public static <T> ComponentType<Integer> registerInt(String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), ComponentType.<Integer>builder().codec(Codec.INT).build());
    }

    public static <T> ComponentType<String> registerString(String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), ComponentType.<String>builder().codec(Codec.STRING).build());
    }

    public static <T> ComponentType<Float> registerFloat(String name) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, BoundlessAPI.identifier(name), ComponentType.<Float>builder().codec(Codec.FLOAT).build());
    }

    public static void initialize() {}
}
