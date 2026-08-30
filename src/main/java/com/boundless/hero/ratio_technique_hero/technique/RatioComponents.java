package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class RatioComponents {
    public static ComponentType<RatioSkillcheck> RATIO_SKILLCHECK = DataComponentRegistry.registerComponent("ratio_skillcheck", builder -> ComponentType.<RatioSkillcheck>builder().codec(RatioSkillcheck.CODEC));
    public static ComponentType<Boolean> RATIO_NEXT_ATTACK = DataComponentRegistry.registerComponent("ratio_next_attack", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static void initialize(){}
}
