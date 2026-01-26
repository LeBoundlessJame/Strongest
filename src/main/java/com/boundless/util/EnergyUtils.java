package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class EnergyUtils {
    public static void changeEnergyPercentage(PlayerEntity player, float percentage) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        float energy = Math.clamp(stack.getOrDefault(DataComponentRegistry.ENERGY_METER, 100f) + percentage, 0f, 100f);
        stack.set(DataComponentRegistry.ENERGY_METER, energy);
    }

    public static void resetEnergyToMax(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(DataComponentRegistry.ENERGY_METER, 100f);
    }

    public static float getEnergyMeter(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ENERGY_METER, 100f);
    }

}
