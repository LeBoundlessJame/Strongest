package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class CursedEnergyManager {
    public static void setCursedEnergy(LivingEntity user, int cursedEnergy) {
        int clampedCE = MathHelper.clamp(cursedEnergy, 0, getMaxCursedEnergy(user));
        HeroUtils.getHeroStack(user).set(DataComponentRegistry.CURSED_ENERGY, clampedCE);
    }

    public static void addCursedEnergy(LivingEntity user, int cursedEnergy) {
        setCursedEnergy(user, getCursedEnergy(user) + cursedEnergy);
    }

    public static int getMaxCursedEnergy(LivingEntity user) {
        return HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.CURSED_ENERGY_MAX, 0);
    }

    public static int getCursedEnergy(LivingEntity user) {
        return HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.CURSED_ENERGY, getMaxCursedEnergy(user));
    }

    public static void regenCursedEnergyTick(LivingEntity user) {
        if (user.getWorld().isClient || user.age % 20 != 0) return;
        addCursedEnergy(user, getCursedEnergyRegen(user));
    }

    // Todo: un-hardcode this soon, will probably make a system where the more CE you have max, slower it regens
    public static int getCursedEnergyRegen(LivingEntity user) {
        return 33;
    }
}
