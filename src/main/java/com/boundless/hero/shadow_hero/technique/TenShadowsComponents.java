package com.boundless.hero.shadow_hero.technique;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class TenShadowsComponents {
    public static ComponentType<Boolean> SHIKIGAMI_ORDER_MODE = DataComponentRegistry.registerBoolean("shikigami_order_mode");
}
