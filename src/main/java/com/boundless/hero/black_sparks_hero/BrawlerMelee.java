package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.action.AdvancedAttack;
import com.boundless.action.SingleAttack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import static com.boundless.hero.black_sparks_hero.BrawlerHero.DAMAGE;

public class BrawlerMelee {
    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        SingleAttack hook = SingleAttack.builder()
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

        // Todo: attackCount % 2 == 0
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, true, true, 3000);

        Action divergentFist = Action.builder()
                .scheduledTask(4, (user, action) -> MeleeUtils.basicHit(user, action, 20f))
                .scheduledTask(15, (user, action) -> MeleeUtils.basicHit(user, action, 80f))
                .build();

        AttackUtils.startAttackTimer(player, 20);
        ActionUtils.performAction(player, divergentFist);

                /*
        AdvancedAttack divergentFist = AdvancedAttack.builder()
                .player(player)
                .hit(4, (user, action) -> MeleeUtils.basicHit(20f, hook, ))
                .build();

                 */

        /*
           Attack divergentFist = Attack.builder()
                .player(player)
                .damage(DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .hit(4, (player, action) -> { ActionUtils.basicHit(20f) })
                .hit(15, BrawlerMelee::divergentImpact)
                .attackDuration(15)
                .build();

        AttackUtils.performAttack(divergentFist);

         */

        /*

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistPunch.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(15, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;
            if (BlackFlashAbility.calculateBlackFlash(player)) {
                CombatUtils.attack(heroAction, 0.0f, Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
                BlackFlashAbility.blackFlash(player, 200, 10, heroAction);
                return;
            }

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

         */
    }

    public static void divergentImpact(PlayerEntity player) {

    }

    public static void manjiKick(PlayerEntity player) {
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("manji_kick_parry"), 1f, false, true, 3000);
    }

    public static void blackFlash(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.BLACK_FLASH_CHANCE, 1.0f);
    }
}
