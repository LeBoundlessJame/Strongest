package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;
import static com.boundless.registry.DataComponentRegistry.ATTACK_START;

public class AttackUtils {
    public static void startAttackTimer(LivingEntity user, long duration) {
        ItemStack heroStack = HeroUtils.getHeroStack(user);
        heroStack.set(ATTACK_START, user.getWorld().getTime());
        heroStack.set(ATTACK_END, heroStack.getOrDefault(ATTACK_START, 0L) + duration);
    }

    public static boolean canAttack(LivingEntity user) {
        return HeroUtils.combatModeEnabled(user) && user.getWorld().getTime() >= HeroUtils.getHeroStack(user).getOrDefault(ATTACK_END, 0L);
    }

    public static void toggleCombatMode(LivingEntity user) {
        ItemStack stack = HeroUtils.getHeroStack(user);
        stack.set(DataComponentRegistry.COMBAT_MODE_ENABLED, !HeroUtils.combatModeEnabled(user));
    }
}
