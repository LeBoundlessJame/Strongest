package com.boundless.util;

import com.boundless.action.Action;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;
import static com.boundless.registry.DataComponentRegistry.ATTACK_START;

public class AttackUtils {

    public static void triggerAttackAction(PlayerEntity player, Action action) {
        triggerAttackAction(player, action, true);
    }

    public static void triggerAttackAction(PlayerEntity player, Action action, boolean startAttackTimer) {
        List<Integer> keys = new ArrayList<>(action.scheduledTasks.keySet());
        int lifetime = keys.getLast();

        if (startAttackTimer){
            AttackUtils.startAttackTimer(player, lifetime);
        }

        ActionUtils.performAction(player, action);
        //CombatUtils.performAttack(player, action, duration);
    }

    public static void startAttackTimer(PlayerEntity player, long duration) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        heroStack.set(ATTACK_START, player.getWorld().getTime());
        heroStack.set(ATTACK_END, heroStack.getOrDefault(ATTACK_START, 0L) + duration);
    }

    public static boolean canAttack(PlayerEntity player) {
        return player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L) && !HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.VANILLA_MECHANICS, false);
    }
}
