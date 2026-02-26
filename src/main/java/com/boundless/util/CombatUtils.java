package com.boundless.util;

import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.function.BiConsumer;

// Todo: Some of this class could definitely do with being removed and replaced
public class CombatUtils {

    public static void slow(LivingEntity livingEntity, int duration, int amplifier) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier, false, false, false));
    }

    public static void attack(HeroActionEntity heroAction, float damage, Optional<Identifier> impactVisual, BiConsumer<PlayerEntity, Entity> perEntityLogic) {
        heroAction.repositionBox();
        if (heroAction.getOwner() == null) return;
        PlayerEntity player = (PlayerEntity) heroAction.getOwner();

        for (LivingEntity target : heroAction.getWorld().getEntitiesByClass(LivingEntity.class, heroAction.getBoundingBox(), entity -> true)) {
            if (target != player) {
                if (perEntityLogic != null) {
                    perEntityLogic.accept(player, target);
                }
                impactVisual.ifPresent((identifier) -> playImpactVisual(player, target, impactVisual.get()));
                target.damage(target.getDamageSources().generic(), damage);
            }
        }
    }

    public static void attack(HeroActionEntity heroAction, float damage, Optional<Identifier> impactVisual) {
        CombatUtils.attack(heroAction, damage, impactVisual, null);
    }

    public static void perEnemyLogic(HeroActionEntity heroAction, BiConsumer<PlayerEntity, LivingEntity> logic) {
        heroAction.repositionBox();
        if (heroAction.getOwner() == null) return;
        PlayerEntity player = (PlayerEntity) heroAction.getOwner();

        for (LivingEntity target : heroAction.getWorld().getEntitiesByClass(LivingEntity.class, heroAction.getBoundingBox(), entity -> true)) {
            if (target != player) {
                logic.accept(player, target);
            }
        }
    }

    public static void aoeAttack(PlayerEntity player, float radius, BiConsumer<PlayerEntity, LivingEntity> logic) {
        for (LivingEntity target : player.getWorld().getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(radius), entity -> true)) {
            if (target != player) {
                logic.accept(player, target);
            }
        }
    }

    public static void knockbackAttack(HeroActionEntity heroAction, float damage, Optional<Identifier> impactVisual) {
        heroAction.repositionBox();
        if (heroAction.getOwner() == null) return;
        PlayerEntity player = (PlayerEntity) heroAction.getOwner();

        for (LivingEntity target : heroAction.getWorld().getEntitiesByClass(LivingEntity.class, heroAction.getBoundingBox(), entity -> true)) {
            if (target != player) {
                impactVisual.ifPresent((identifier) -> playImpactVisual(player, target, impactVisual.get()));
                target.damage(target.getDamageSources().generic(), damage);
                CombatUtils.uppercutKnockback(player, target);
            }
        }
    }

    public static void knockback(PlayerEntity attacker, LivingEntity target, float strength) {
        Vec3d attackerRotation = attacker.getRotationVector();
        target.takeKnockback(strength, attackerRotation.x * -1, attackerRotation.z * -1);
        target.velocityModified = true;
    }

    // Bypasses knockback reduction
    public static void strongKnockback(PlayerEntity player, LivingEntity target, float strength) {
        target.setVelocity(player.getRotationVector().x * strength, 1, player.getRotationVector().z * strength);
        target.velocityModified = true;
    }

    public static void uppercutKnockback(PlayerEntity player, LivingEntity target) {
        target.setVelocity(player.getRotationVector().x * 1.2, 1, player.getRotationVector().z * 1.2);
        target.velocityModified = true;
    }

    public static void playImpactVisual(PlayerEntity player, LivingEntity target, Identifier impactVisual) {
        Vec3d effectScale =  new Vec3d(target.getScale() * 0.5f, target.getScale() * 0.5f, target.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(impactVisual, player, target.getPos().add(0, target.getHeight() / 2, 0), effectScale, effectRotation);
    }

    public static boolean isRolling(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ROLLING_END, 0L);
    }
}
