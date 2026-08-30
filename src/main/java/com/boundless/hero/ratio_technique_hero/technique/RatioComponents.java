package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.component.ComponentType;

public class RatioComponents {
    public static ComponentType<RatioSkillcheck> RATIO_SKILLCHECK = DataComponentRegistry.registerComponent("ratio_skillcheck", builder -> ComponentType.<RatioSkillcheck>builder().codec(RatioSkillcheck.CODEC));
    public static void initialize(){}
}
