package com.boundless.util;

import com.boundless.ability.TechniqueAbility;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CombatUtils {

    public static void hit(PlayerEntity player, HeroActionEntity action, float baseDamage, Vec3d knockback, BiConsumer<PlayerEntity, LivingEntity> onHit, HitEffects hitEffects) {
        action.repositionBox();

        List<LivingEntity> targets = getTargets(player, action, entity -> true);
        if (targets.isEmpty()) return;

        AttackContext context = AttackResolver.resolveAttack(player);

        for (LivingEntity target: targets) {
            Hit hit = new Hit(player, target, action, baseDamage, knockback, new HitEffects(hitEffects));
            hit = AttackResolver.resolveHit(hit, context);

            applyHit(hit);
            // Todo: I might make this override the existing damage logic or something but we'll see
            onHit.accept(player, target);
        }
    }

    public static void applyHit(Hit hit) {
        PlayerEntity attacker = hit.getAttacker();
        LivingEntity target = hit.getTarget();

        hit.getHitEffects().playEffects(attacker, target);

        Vec3d knockbackMultiplier = hit.getKnockback();
        target.damage(target.getDamageSources().generic(), hit.getDamage());
        System.out.println(hit.getDamage());
        target.setVelocity(attacker.getRotationVector().x * knockbackMultiplier.x, knockbackMultiplier.y, attacker.getRotationVector().z * knockbackMultiplier.z);
        target.velocityModified = true;
    }

    public static List<LivingEntity> getTargets(PlayerEntity player, HeroActionEntity action, Predicate<LivingEntity> predicate) {
        return action.getWorld().getEntitiesByClass(LivingEntity.class, action.getBoundingBox(), entity -> isValidTarget(player, entity) && predicate.test(entity));
    }

    public static List<LivingEntity> getTargets(PlayerEntity player, HeroActionEntity action) {
        return getTargets(player, action, entity -> true);
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
