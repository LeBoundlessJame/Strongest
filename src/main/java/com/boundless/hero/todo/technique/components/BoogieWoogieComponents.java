package com.boundless.hero.todo.technique.components;

import com.boundless.hero.nanami.technique.RatioSkillcheck;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class BoogieWoogieComponents {
    public static ComponentType<Integer> PRIMARY_TARGET_ID = DataComponentRegistry.registerComponent("primary_target", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> SECONDARY_TARGET_ID = DataComponentRegistry.registerComponent("secondary_target_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static void initialize(){}
}
