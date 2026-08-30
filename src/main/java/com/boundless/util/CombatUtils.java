package com.boundless.util;

import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CombatUtils {
    public static List<LivingEntity> getTargets(PlayerEntity player, HeroActionEntity action, Predicate<LivingEntity> predicate) {
        return action.getWorld().getEntitiesByClass(LivingEntity.class, action.getBoundingBox(), entity -> isValidTarget(player, entity) && predicate.test(entity));
    }

    public static void slow(LivingEntity livingEntity, int duration, int amplifier) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier, false, false, false));
    }

    public static void playImpactVisual(PlayerEntity player, LivingEntity target, Identifier impactVisual) {
        Vec3d effectScale =  new Vec3d(target.getScale() * 0.5f, target.getScale() * 0.5f, target.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(impactVisual, player, target.getPos().add(0, target.getHeight() / 2, 0), effectScale, effectRotation);
    }

    public static boolean isRolling(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ROLLING_END, 0L);
    }

    private static boolean isValidTarget(PlayerEntity player, LivingEntity entity) {
        return player != entity && (!(entity instanceof TameableEntity tameable && tameable.getOwner() == player));
    }
}
