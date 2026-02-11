package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class DropkickLogic {

    public static void dropkick(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(BrawlerHero.DROPKICK_DAMAGE_TRIGGERED, false);
        stack.set(BrawlerHero.CHARGED_LEAP_TIME_WINDOW, 0L);

        Entity target = RaycastUtils.thickRaycast(player, 32, 2);
        if (target == null) return;

        int overallDuration = 15;
        int moveToTargetDuration = 8;

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        for (int i = 0; i < overallDuration; i++) {
            int remainingMoveTicks = moveToTargetDuration - i;

            tasks.put(i, (user, heroAction) -> {
                if (stack.getOrDefault(BrawlerHero.DROPKICK_DAMAGE_TRIGGERED, false)) return;

                if (user.distanceTo(target) < 5 && !stack.getOrDefault(BrawlerHero.DROPKICK_DAMAGE_TRIGGERED, false)) {
                    stack.set(BrawlerHero.DROPKICK_DAMAGE_TRIGGERED, true);
                    CombatUtils.aoeAttack(player, 4, DropkickLogic::dropkickAoe);
                    return;
                }

                if (remainingMoveTicks > 0) {
                    Vec3d velocity = target.getPos().subtract(user.getPos()).multiply(1.0 / remainingMoveTicks);
                    user.setVelocity(velocity);
                    user.velocityModified = true;
                    user.velocityDirty = true;
                }
            });
        }

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("dropkick"), 2f, false, true, 5000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, 15);
    }

    public static void dropkickAoe(PlayerEntity player, LivingEntity target) {
        SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
        SoundUtils.playSound(player, SoundRegistry.ROCK_CRUMBLING);

        Vec3d effectScale = new Vec3d(player.getScale() * 0.5f, player.getScale() * 0.5f, player.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(BoundlessAPI.identifier("melee_impact_crit"), player, target.getPos().add(0, target.getHeight() / 2, 0), effectScale, effectRotation);

        CameraUtils.playCameraShake(player);
        target.damage(target.getDamageSources().generic(), BrawlerHero.DAMAGE.spinKick.get());
        CombatUtils.uppercutKnockback(player, target);
        CombatUtils.playImpactVisual(player, target, BoundlessAPI.identifier("landing_impact"));
    }

    public static boolean canDropkick(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(BrawlerHero.CHARGED_LEAP_TIME_WINDOW, 0L);
    }
}
