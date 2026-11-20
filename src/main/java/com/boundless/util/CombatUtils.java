package com.boundless.util;

import com.boundless.combat.AttackContext;
import com.boundless.combat.AttackResolver;
import com.boundless.combat.Hit;
import com.boundless.combat.HitEffects;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CombatUtils {
    public static void hitInRadius(LivingEntity user, Vec3d center, Vec3d radius, float baseDamage, Vec3d knockback, BiConsumer<LivingEntity, LivingEntity> onHit, HitEffects hitEffects) {
        List<LivingEntity> targets = AOEUtils.getTargetsInRadius(user, user.getWorld(), center, radius, entity -> true);
        applyHits(user, targets, baseDamage, knockback, onHit, hitEffects);
    }

    public static void resolveAndApplyHit(Hit hit, AttackContext context) {
        hit = AttackResolver.resolveHit(hit, context);
        applyHit(hit);
    }

    public static void applyHits(LivingEntity user, List<LivingEntity> targets, float baseDamage, Vec3d knockback, BiConsumer<LivingEntity, LivingEntity> onHit, HitEffects hitEffects) {
        if (targets.isEmpty()) return;
        AttackContext context = AttackResolver.resolveAttack(user);

        for (LivingEntity target: targets) {
            Hit hit = new Hit(user, target, baseDamage, knockback, new HitEffects(hitEffects));
            resolveAndApplyHit(hit, context);

            onHit.accept(user, target);
        }

        context.postTrigger(user);
    }

    public static void applyHit(Hit hit) {
        LivingEntity attacker = hit.getAttacker();
        LivingEntity target = hit.getTarget();

        hit.getHitEffects().playEffects(attacker, target);

        Vec3d knockbackMultiplier = hit.getKnockback();
        target.damage(target.getDamageSources().generic(), hit.getDamage());
        target.setVelocity(attacker.getRotationVector().x * knockbackMultiplier.x, knockbackMultiplier.y, attacker.getRotationVector().z * knockbackMultiplier.z);
        target.velocityModified = true;
    }

    public static void slow(LivingEntity livingEntity, int duration, int amplifier) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, amplifier, false, false, false));
    }

    public static void playImpactVisual(LivingEntity user, LivingEntity target, Identifier impactVisual) {
        Vec3d effectScale =  new Vec3d(target.getScale() * 0.5f, target.getScale() * 0.5f, target.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(user.getPitch(), user.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(impactVisual, user, target.getPos().add(0, target.getHeight() / 2, 0), effectScale, effectRotation);
    }

    public static boolean isRolling(LivingEntity user) {
        return user.getWorld().getTime() <= HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.ROLLING_END, 0L);
    }

    public static boolean isValidTarget(LivingEntity user, LivingEntity entity) {
        return entity.isAlive() && user != entity && (!(entity instanceof TameableEntity tameable && tameable.getOwner() == user));
    }
}
