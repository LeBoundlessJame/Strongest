package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.action.Attack;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import static com.boundless.hero.black_sparks_hero.BlackSparksHero.COOLDOWNS;

public class ShrineHeroSlashes {
    public static Ability DISMANTLE = AbilityUtils.ability(ShrineHeroSlashes::dismantle, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("dismantle"), "Dismantle");
    public static Ability CLEAVE = AbilityUtils.ability(ShrineHeroSlashes::cleave, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("cleave"), "Cleave");
    public static Ability SPIDERWEB_CLEAVE = AbilityUtils.ability(ShrineHeroSlashes::spiderwebCleave, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("spiderweb_cleave"), "Spider-Web Cleave");
    public static Ability DISMANTLE_BARRAGE = AbilityUtils.ability(ShrineHeroSlashes::dismantleBarrage, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("dismantle_barrage"), "Dismantle Barrage");

    public static void cleave(PlayerEntity player) {
        Attack cleave = Attack.builder().player(player).damage(5f).impactSound(SoundRegistry.EARTH_IMPACT).animationSpeed(1.0f).damage(12f).animation(BoundlessAPI.identifier("cleave")).impactTick(4).attackDuration(8).perEntityLogic((user, entity) -> {
            CameraUtils.playCameraShake(user);
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVERT_EFFECT, 10, 4, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 2, true, false, false));

            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(livingEntity.getDamageSources().generic(), livingEntity.getMaxHealth() * 0.10f);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 4, true, false, false));
                EffekUtils.playRotatedEffect(BoundlessAPI.identifier("upgraded_cleave"), livingEntity, new Vec3d(livingEntity.getX() - player.getRotationVector().x, livingEntity.getBodyY(0.5), livingEntity.getZ() - player.getRotationVector().z), new Vec3d(livingEntity.getHeight() / 10, livingEntity.getHeight() / 10, livingEntity.getHeight() / 10), new Vec3d(0, 0, player.getRotationVector().z));
                EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
            }
        }).build();

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 1, true, false, false));
        AttackUtils.performAttack(cleave);
    }

    public static void dismantleBarrage(PlayerEntity player) {
        /*
        EffekUtils.playEffect(BoundlessAPI.identifier("surehit_rendition"), player, player.getPos().add(0f, 0.1f, 0f).add(player.getRotationVector().normalize().multiply(10)), 2.0f);
         */

        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();
        if (!(target instanceof LivingEntity livingEntity)) return;
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 5, false, false, false));

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> slashes = new LinkedHashMap<>();

        for (int i = 0; i < 40; i++) {
            slashes.put(i, (user, heroAction) -> {
                livingEntity.timeUntilRegen = 0;
                livingEntity.damage(player.getDamageSources().generic(), 2);
            });
        }

        Action barrage = Action.builder().scheduledTasks(slashes).build();
        ActionUtils.performAction(player, barrage);

        EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_barrage"), player, target.getPos(), 1.0f);
    }

    public static void spiderwebCleave(PlayerEntity player) {
        Attack cleave = Attack.builder().player(player).damage(5f).impactSound(SoundRegistry.EARTH_IMPACT).animationSpeed(1.0f).damage(12f).animation(BoundlessAPI.identifier("spiderweb_cleave")).impactTick(4).attackDuration(8).perEntityLogic((user, entity) -> {
            CameraUtils.playCameraShake(user);
            if (entity instanceof LivingEntity livingEntity) {
                EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
                livingEntity.damage(livingEntity.getDamageSources().generic(), 18);
                livingEntity.setVelocity(player.getRotationVector().x * 1.2, 1, player.getRotationVector().z * 1.2);
                livingEntity.velocityModified = true;
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 10, 3, false, false, false));
            }
        }).build();

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 255, false, false, false));
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spiderweb_cleave"), 2f, false, true, 3000);
        EffekUtils.playEffect(BoundlessAPI.identifier("spiderweb_cleave"), player, player.getPos().add(0, 0.05f, 0), new Vec3d(2, 1, 2));
        AttackUtils.performAttack(cleave);
    }

    public static void dismantle(PlayerEntity player) {
        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 13, 16);

        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target instanceof LivingEntity livingEntity) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0, false, false, false));
            SoundUtils.playSound(player, SoundRegistry.HEAVY_CUT_3, 13, 16);
            livingEntity.timeUntilRegen = 0;
            livingEntity.damage(livingEntity.getDamageSources().generic(), 20f);

            float force = livingEntity.isOnGround() ? 1.2f : 2f;
            livingEntity.setVelocity(player.getRotationVector().x * force, 0, player.getRotationVector().z * force);
            livingEntity.velocityModified = true;
            SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 10, 11);
            EffekUtils.playRandomRotatedEffect(BoundlessAPI.identifier("upgraded_dismantle"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), new Vec3d(1, 1, 1));
            EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
            //CameraUtils.playCameraShake(player);
        }
    }
}
