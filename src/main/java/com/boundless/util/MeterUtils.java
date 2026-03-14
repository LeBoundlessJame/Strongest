package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class MeterUtils {
    public static float getRemainingMeter(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 1.0f);
    }

    public static void consumeMeter(PlayerEntity player, float amount) {
        float meterRemaining = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 1.0f);
        meterRemaining = Math.round(meterRemaining * 100f) / 100f;
        float updatedMeterValue = Math.clamp(meterRemaining - amount, 0.0f, 1.0f);
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.METER, updatedMeterValue);
    }

    public static void regenMeter(PlayerEntity player, float amount) {
        float meterRemaining = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 1.0f);
        meterRemaining = Math.round(meterRemaining * 100f) / 100f;
        float updatedMeterValue = Math.clamp(meterRemaining + amount, 0.0f, 1.0f);
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.METER, updatedMeterValue);
    }
}
