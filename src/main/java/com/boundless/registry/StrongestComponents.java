package com.boundless.registry;

import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.megumi.technique.abilities.ShikigamiOrdersMenuAbility;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class StrongestComponents {
    public static ComponentType<Float> BLACK_FLASH_CHANCE = DataComponentRegistry.registerComponent("black_flash_chance", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Float> BLACK_FLASH_DAMAGE_MULTIPLIER = DataComponentRegistry.registerComponent("black_flash_damage_multiplier", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<Float> MELEE_STRENGTH = DataComponentRegistry.registerComponent("melee_strength", builder -> ComponentType.<Float>builder().codec(Codec.FLOAT));
    public static ComponentType<String> MELEE_COMBO = DataComponentRegistry.registerComponent("melee_combo", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static ComponentType<Boolean> MENU_TOGGLE = DataComponentRegistry.registerComponent("menu_toggle", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static void initialize() {}
}
