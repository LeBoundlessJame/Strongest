package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.action.AdvancedAttack;
import com.boundless.action.SingleAttack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import static com.boundless.hero.black_sparks_hero.BrawlerHero.DAMAGE;

public class BrawlerMelee {
    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, true, true, 3000);

        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        Action hook = Action.builder()
                .scheduledTask(4, (user, action) -> {
                    MeleeUtils.forEach(player, action, (attacker, entity) -> {
                        if (BlackFlashAbility.calculateBlackFlash(attacker)) {
                            // Todo: make it so that upwards knockback is optional
                            BlackFlashAbility.blackFlash(attacker, 80, new Vec3d(0.2f, 0.0f, 0.2f), action);
                            return;
                        }
                        MeleeUtils.basicHit(user, action, DAMAGE.lightAttack.get());
                    });
                })
                .build();

        AttackUtils.startAttackTimer(player, 4);
        ActionUtils.performAction(player, hook);
    }

    // Todo: make some pre, post and replacement 'events' for attacks
    public static void divergentFist(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        // Todo: attackCount % 2 == 0
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, true, true, 3000);

        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        Action divergentFist = Action.builder()
                .scheduledTask(4, (user, action) -> MeleeUtils.basicHit(user, action, 20f))
                .scheduledTask(20, (user, action) -> BrawlerMelee.divergentImpact(user, action, 80f))
                .build();

        AttackUtils.startAttackTimer(player, 20);
        ActionUtils.performAction(player, divergentFist);
    }

    public static void divergentImpact(PlayerEntity player, HeroActionEntity action, float damage) {
        CameraUtils.playCameraShake(player);

        MeleeUtils.forEach(player, action, (user, entity) -> {
            if (BlackFlashAbility.calculateBlackFlash(player)) {
                CombatUtils.playImpactVisual(player, (LivingEntity) entity, BoundlessAPI.identifier("divergent_fist_impact"));
                BlackFlashAbility.blackFlash(player, 200, new Vec3d(10f, 1.0f, 10f), action);
                return;
            }

            entity.timeUntilRegen = 0;
            entity.damage(entity.getDamageSources().generic(), damage);
            if (!(entity instanceof LivingEntity livingEntity)) return;

            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);
            CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("divergent_fist_impact"));
            CombatUtils.strongKnockback(player, livingEntity, 5);
        });
    }

    public static void manjiKick(PlayerEntity player) {
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("manji_kick_parry"), 1f, false, true, 3000);
    }

    public static void blackFlash(PlayerEntity player) {
        EffekUtils.playBoundEffect(BoundlessAPI.identifier("black_flash_aura"), player, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0, 0, 0));

        HeroUtils.getHeroStack(player).set(StrongestComponents.BLACK_FLASH_CHANCE, 1.0f);
    }
}
