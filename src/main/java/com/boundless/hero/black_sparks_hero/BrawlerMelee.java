package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.action.Attack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.boundless.hero.black_sparks_hero.BrawlerHero.DAMAGE;

public class BrawlerMelee {
    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        Attack hook = Attack.builder()
                .player(player)
                .damage(DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }

    // Todo: make some pre, post and replacement 'events' for attacks
    public static void divergentFist(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistPunch.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(15, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);

            CameraUtils.playCameraShake(player);
            CombatUtils.perEnemyLogic(heroAction, (attacker, livingEntity) -> {
                livingEntity.timeUntilRegen = 0;
            });
            CombatUtils.attack(heroAction, DAMAGE.divergentFistImpact.get(), Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
        AttackUtils.startAttackTimer(player, 10);
    }

    public static void manjiKick(PlayerEntity player) {
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("manji_kick_parry"), 1f, false, true, 3000);
    }
}
