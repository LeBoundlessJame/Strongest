package com.boundless.util;

import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

public class CombatUtils {
    public static void attack(HeroActionEntity heroAction, float damage, Optional<Identifier> impactVisual) {
        heroAction.repositionBox();
        if (heroAction.getOwner() == null) return;
        PlayerEntity player = (PlayerEntity) heroAction.getOwner();

        for (LivingEntity target : heroAction.getWorld().getEntitiesByClass(LivingEntity.class, heroAction.getBoundingBox(), entity -> true)) {
            if (target != player) {
                impactVisual.ifPresent((identifier) -> playImpactVisual(player, target, impactVisual.get()));
                target.damage(target.getDamageSources().generic(), damage);
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

    public static void attack(HeroActionEntity heroAction, AttackDataBuilder attackDataBuilder) {
        heroAction.repositionBox();
        boolean hasPlayedSound = false;

        if (heroAction.getOwner() == null) return;
        PlayerEntity player = (PlayerEntity) heroAction.getOwner();

        if (attackDataBuilder.getReplacedAttackLogic() != null) {
            attackDataBuilder.getReplacedAttackLogic().accept(attackDataBuilder, player);
        }

        for (LivingEntity livingEntity : heroAction.getWorld().getEntitiesByClass(LivingEntity.class, heroAction.getBoundingBox(), entity -> true)) {
            if (livingEntity != player) {
                if (attackDataBuilder.getImpactVisual() != null) {
                    playImpactVisual(player, livingEntity, attackDataBuilder.getImpactVisual());
                }

                if (!hasPlayedSound && attackDataBuilder.getImpactSound() != null) {
                    player.getWorld().playSound(null, livingEntity.getBlockPos(), attackDataBuilder.getImpactSound(), SoundCategory.PLAYERS, 1f, 1f);
                    hasPlayedSound = true;
                }

                if (attackDataBuilder.getCustomHitLogic() != null) {
                    attackDataBuilder.getCustomHitLogic().accept(attackDataBuilder, livingEntity);
                } else {
                    CombatUtils.basicAttackLogic(attackDataBuilder, livingEntity);
                }

                if (attackDataBuilder.getPostHitLogic() != null) {
                    attackDataBuilder.getPostHitLogic().accept(attackDataBuilder, player);
                }
            }
        }

        if (!hasPlayedSound && attackDataBuilder.getMissSound() != null) {
            player.getWorld().playSound(null, heroAction.getBlockPos(), attackDataBuilder.getMissSound(), SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    public static void basicAttackLogic(AttackDataBuilder attack, LivingEntity target) {
        DamageSource source = attack.getDamageSource();
        if (source == null) source = target.getDamageSources().generic();
        target.damage(source, attack.getDamage());
        knockback(attack, target);
    }

    public static void knockback(AttackDataBuilder attack, LivingEntity target) {
        PlayerEntity attacker = attack.getAttacker();
        Vec3d attackerRotation = attacker.getRotationVector();
        target.takeKnockback(attack.getKnockbackStrength(), attackerRotation.x * -1, attackerRotation.z * -1);
        target.velocityModified = true;
    }

    public static void uppercutKnockback(PlayerEntity player, LivingEntity target) {
        target.setVelocity(player.getRotationVector().x * 1.2, 1, player.getRotationVector().z * 1.2);
        target.velocityModified = true;
    }

    public static boolean canAttack(PlayerEntity player) {
        return player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L) && !HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.VANILLA_MECHANICS, false);
    }

    public static void playImpactVisual(PlayerEntity player, LivingEntity target, Identifier impactVisual) {
        Vec3d effectScale =  new Vec3d(target.getScale() * 0.5f, target.getScale() * 0.5f, target.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(impactVisual, player, target.getPos().add(0, target.getHeight() / 2, 0), effectScale, effectRotation);
    }
}
