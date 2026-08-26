package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

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
}
