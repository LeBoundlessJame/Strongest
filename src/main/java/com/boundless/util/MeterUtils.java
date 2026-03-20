package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StrongestComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class MeterUtils {
    public static int getRemainingMeter(PlayerEntity player, int fallback) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.CURSED_ENERGY, fallback);
    }

    public static void consumeMeter(PlayerEntity player, int amount, int max) {
        if (player.getWorld().isClient) return;
        ItemStack stack = HeroUtils.getHeroStack(player);

        int meter = stack.getOrDefault(StrongestComponents.CURSED_ENERGY, max);
        meter = MathHelper.clamp(meter - amount, 0, max);
        stack.set(StrongestComponents.CURSED_ENERGY, meter);
    }

    // Todo: make fallback not hard coded
    public static void regenMeter(PlayerEntity player, int amount) {
        if (player.getWorld().isClient) return;

        int meter = getRemainingMeter(player, 10000);
        meter = MathHelper.clamp(meter + amount, 0, 10000);
        HeroUtils.getHeroStack(player).set(StrongestComponents.CURSED_ENERGY, meter);
    }

    public static void regenMeterBasedOnHealth(PlayerEntity player, int min, int max) {
        int amount = MathHelper.lerp(player.getHealth() / player.getMaxHealth(), min, max);
        regenMeter(player, amount);
    }
}
