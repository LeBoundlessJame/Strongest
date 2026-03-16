package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.function.BiConsumer;

public class MeleeUtils {
    public static void basicHit(PlayerEntity player, HeroActionEntity action, float damage) {
        MeleeUtils.forEach(player, action, (user, entity) -> {
            entity.damage(entity.getDamageSources().generic(), damage);
            if (!(entity instanceof LivingEntity livingEntity)) return;

            CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("melee_impact"));
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
        });
    }

    public static void forEach(PlayerEntity player, HeroActionEntity action, BiConsumer<PlayerEntity, Entity> logic) {
        for (LivingEntity target : action.getWorld().getEntitiesByClass(LivingEntity.class, action.getBoundingBox(), entity -> true)) {
            if (target != player) logic.accept(player, target);
        }
    }

    public static void knockback(PlayerEntity player, LivingEntity target, Vec3d knockbackMultiplier) {
        target.setVelocity(player.getRotationVector().x * knockbackMultiplier.x,  1 * knockbackMultiplier.y, player.getRotationVector().z * knockbackMultiplier.z);
        target.velocityModified = true;
    }

    /** Put all players abilities on cooldown temporarily **/
    public static void disorient(PlayerEntity player, int cooldownDuration) {
        disorientWithExemption(player, cooldownDuration, null);
    }

    /** When you want to disorient all abilities except one **/
    public static void disorientWithExemption(PlayerEntity player, int cooldownDuration, Identifier exemptAbility) {
        Map<String, Identifier> abilities = HeroUtils.getHeroStack(player).get(DataComponentRegistry.ABILITY_LOADOUT);
        if (abilities == null || abilities.isEmpty()) return;
        for (Identifier abilityID: abilities.values()) {
            Long remainingCooldown = AbilityUtils.getRemainingCooldown(player, abilityID);
            if (remainingCooldown != null && !abilityID.equals(exemptAbility)) {
                if (remainingCooldown < cooldownDuration) {
                    AbilityUtils.setAbilityCooldown(player, abilityID, cooldownDuration);
                }
            }
        }
    }

    public static boolean isFinalLightAttack(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.LIGHT_ATTACK_COUNTER, 0) % 5 == 0;
    }
}
