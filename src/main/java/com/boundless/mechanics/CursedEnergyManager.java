package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class CursedEnergyManager {
    public static void setCursedEnergy(PlayerEntity player, int cursedEnergy) {
        int clampedCE = MathHelper.clamp(cursedEnergy, 0, getMaxCursedEnergy(player));
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.CURSED_ENERGY, clampedCE);
    }

    public static void addCursedEnergy(PlayerEntity player, int cursedEnergy) {
        setCursedEnergy(player, getCursedEnergy(player) + cursedEnergy);
    }

    public static int getMaxCursedEnergy(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.CURSED_ENERGY_MAX, 0);
    }

    public static int getCursedEnergy(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.CURSED_ENERGY, getMaxCursedEnergy(player));
    }

    public static void regenCursedEnergyTick(PlayerEntity player) {
        if (player.getWorld().isClient || player.age % 20 != 0) return;
        addCursedEnergy(player, getCursedEnergyRegen(player));
    }

    // Todo: un-hardcode this soon, will probably make a system where the more CE you have max, slower it regens
    public static int getCursedEnergyRegen(PlayerEntity player) {
        return 33;
    }
}
