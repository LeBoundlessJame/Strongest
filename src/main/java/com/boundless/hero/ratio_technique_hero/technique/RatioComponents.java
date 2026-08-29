package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

import static com.boundless.registry.DataComponentRegistry.registerComponent;

public class RatioComponents {
    public static ComponentType<RatioSkillcheck> RATIO_SKILLCHECK = DataComponentRegistry.registerComponent("ratio_skillcheck", builder -> ComponentType.<RatioSkillcheck>builder().codec(RatioSkillcheck.CODEC));
    public static void initialize(){}
}
