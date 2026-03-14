package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class MeterUtils {
    public static int getRemainingMeter(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 100);
    }

    public static void consumeMeter(PlayerEntity player, int amount) {
        int meter = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 100);
        meter = MathHelper.clamp(meter - amount, 0, 100);
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.METER, meter);
    }

    public static void regenMeter(PlayerEntity player, int amount) {
        int meter = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 100);
        meter = MathHelper.clamp(meter + amount, 0, 100);
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.METER, meter);
    }

}
