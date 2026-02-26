package com.boundless.util;

import com.boundless.action.Action;
import com.boundless.action.SingleAttack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import javax.xml.crypto.Data;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;
import static com.boundless.registry.DataComponentRegistry.ATTACK_START;

public class AttackUtils {
    public static void performAttack(SingleAttack singleAttack) {
        PlayerEntity player = singleAttack.getPlayer();
        if (!AttackUtils.canAttack(player)) return;

        float damage = singleAttack.getDamage();
        SoundEvent sound = singleAttack.getImpactSound();
        Identifier animation = singleAttack.getAnimation();
        Identifier attackVFX = singleAttack.getImpactVFX();
        float animationSpeed = singleAttack.getAnimationSpeed();
        int priority = singleAttack.getAnimationPriority();
        int attackDuration = singleAttack.getAttackDuration();
        int impactTick = singleAttack.getImpactTick();
        BiConsumer<PlayerEntity, Entity> perEntityLogic = singleAttack.getPerEntityLogic();

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> hit = (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;
            SoundUtils.playSound(player, sound);
            CombatUtils.attack(heroAction, damage, Optional.of(attackVFX), perEntityLogic);
        };
        tasks.put(impactTick, hit);
        AnimationUtils.playSyncedAnimation(player, animation, animationSpeed, attackCount % 2 == 0, true, priority);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, attackDuration);
    }

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

    public static int incrementedAttackCount(PlayerEntity player) {
        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
    }
}
