package com.boundless.util;

import com.boundless.registry.StrongestComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class MeterUtils {
    public static int getRemainingMeter(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.CURSED_ENERGY, getMaxCE(player));
    }

    public static void consumeMeter(PlayerEntity player, int amount) {
        if (player.getWorld().isClient) return;
        ItemStack stack = HeroUtils.getHeroStack(player);

        int max = stack.getOrDefault(StrongestComponents.CURSED_ENERGY_RESERVES, getMaxCE(player));
        int meter = stack.getOrDefault(StrongestComponents.CURSED_ENERGY, max);
        meter = MathHelper.clamp(meter - amount, 0, max);
        stack.set(StrongestComponents.CURSED_ENERGY, meter);
    }

    // Todo: make fallback not hard coded
    public static void regenMeter(PlayerEntity player, int amount) {
        if (player.getWorld().isClient) return;

        int meter = getRemainingMeter(player);
        meter = MathHelper.clamp(meter + amount, 0, getMaxCE(player));
        HeroUtils.getHeroStack(player).set(StrongestComponents.CURSED_ENERGY, meter);
    }

    public static Integer getMaxCE(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.CURSED_ENERGY_RESERVES, 0);
    }
}
