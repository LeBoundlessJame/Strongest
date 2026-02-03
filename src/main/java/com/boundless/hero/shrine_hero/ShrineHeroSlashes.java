package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Attack;
import com.boundless.client.CameraShake;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import static com.boundless.hero.black_sparks_hero.BlackSparksHero.COOLDOWNS;

public class ShrineHeroSlashes {
    public static Ability DISMANTLE = AbilityUtils.ability(ShrineHeroSlashes::dismantle, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("dismantle"), "Dismantle");
    public static Ability CLEAVE = AbilityUtils.ability(ShrineHeroSlashes::cleave, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("cleave"), "Cleave");

    public static void cleave(PlayerEntity player) {
        Attack cleave = Attack.builder()
                .player(player)
                .damage(5f)
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .damage(12f)
                .animation(BoundlessAPI.identifier("cleave"))
                .impactTick(4)
                .attackDuration(8)
                .perEntityLogic((user, entity) -> {
                    CameraUtils.playCameraShake(user);
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(livingEntity.getDamageSources().generic(), livingEntity.getMaxHealth() * 0.10f);
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 4, true, false, false));
                        EffekUtils.playRotatedEffect(BoundlessAPI.identifier("upgraded_cleave"), livingEntity, new Vec3d(livingEntity.getX() - player.getRotationVector().x, livingEntity.getBodyY(0.5), livingEntity.getZ() - player.getRotationVector().z), new Vec3d(livingEntity.getHeight() / 10, livingEntity.getHeight() / 10, livingEntity.getHeight() / 10), new Vec3d(0, 0, player.getRotationVector().z));
                        EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
                    }
                })
                .build();

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 1, true, false, false));
        AttackUtils.performAttack(cleave);
    }


    public static void dismantle(PlayerEntity player) {
        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 13, 16);

        EntityHitResult raycastResult = RaycastUtils.raycast(player, 64);

        if (raycastResult != null && raycastResult.getEntity() instanceof LivingEntity livingEntity) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0, false, false, false));
            livingEntity.timeUntilRegen = 0;
            livingEntity.damage(livingEntity.getDamageSources().generic(), 20f);

            float force = livingEntity.isOnGround() ? 1.2f : 2f;
            livingEntity.setVelocity(player.getRotationVector().x * force, 0, player.getRotationVector().z * force);
            livingEntity.velocityModified = true;
            SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 10, 11);
            EffekUtils.playEffect(BoundlessAPI.identifier("upgraded_dismantle"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 4);
            EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
            CameraUtils.playCameraShake(player);
        }
    }
}
