package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;
import static com.boundless.registry.DataComponentRegistry.ATTACK_START;

public class AttackUtils {
    public static void startAttackTimer(PlayerEntity player, long duration) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        heroStack.set(ATTACK_START, player.getWorld().getTime());
        heroStack.set(ATTACK_END, heroStack.getOrDefault(ATTACK_START, 0L) + duration);
    }

    public static boolean canAttack(PlayerEntity player) {
        return HeroUtils.combatModeEnabled(player) && player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L);
    }

    public static void toggleCombatMode(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(DataComponentRegistry.COMBAT_MODE_ENABLED, !HeroUtils.combatModeEnabled(player));
    }
}
