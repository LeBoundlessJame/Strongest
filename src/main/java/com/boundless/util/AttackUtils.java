package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.action.Attack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;
import static com.boundless.registry.DataComponentRegistry.ATTACK_START;

public class AttackUtils {
    public static void performAttack(Attack attack) {
        PlayerEntity player = attack.getPlayer();
        if (!AttackUtils.canAttack(player)) return;

        float damage = attack.getDamage();
        SoundEvent sound = attack.getImpactSound();
        Identifier animation = attack.getAnimation();
        Identifier attackVFX = attack.getImpactVFX();
        float animationSpeed = attack.getAnimationSpeed();
        int priority = attack.getAnimationPriority();
        int attackDuration = attack.getAttackDuration();
        int impactTick = attack.getImpactTick();

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> hit = (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;
            SoundUtils.playSound(player, sound);
            CombatUtils.attack(heroAction, damage, Optional.of(attackVFX));
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
        return player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L);
    }

    public static int incrementedAttackCount(PlayerEntity player) {
        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        return DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);
    }
}
