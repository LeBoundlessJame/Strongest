package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class MeterUtils {
    public static float getRemainingMeter(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 1.0f);
    }

    public static void consumeMeter(PlayerEntity player, float percentage) {
        float meterRemaining = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.METER, 1.0f);
        if (meterRemaining - percentage >= 0.0){
            float updatedMeterValue = meterRemaining - percentage;
            updatedMeterValue = Math.round(updatedMeterValue * 100f) / 100f;
            HeroUtils.getHeroStack(player).set(DataComponentRegistry.METER, updatedMeterValue);
        }
    }
}
