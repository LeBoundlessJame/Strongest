package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.BiConsumer;

public class MeleeUtils {
    public static void basicHit(PlayerEntity player, HeroActionEntity action, float damage) {
        MeleeUtils.basicHit(player, action, damage, new Vec3d(0.6, 0.3, 0.6));
    }

    public static void basicHit(PlayerEntity player, HeroActionEntity action, float damage, Vec3d knockback) {
        MeleeUtils.forEach(player, action, (user, entity) -> {
            entity.damage(entity.getDamageSources().generic(), damage);
            if (!(entity instanceof LivingEntity livingEntity)) return;

            playCombatEffect(player, livingEntity, BoundlessAPI.id("melee_impact"), SoundRegistry.EARTH_IMPACT);
            knockback(user, livingEntity, knockback);
        });
    }

    // Filters out the attack user
    public static List<LivingEntity> getTargets(PlayerEntity player, HeroActionEntity action) {
        return action.getWorld().getEntitiesByClass(LivingEntity.class, action.getBoundingBox(), entity -> entity != player);
    }

    public static void forEach(PlayerEntity player, HeroActionEntity action, BiConsumer<PlayerEntity, Entity> logic) {
        for (LivingEntity target : getTargets(player, action)) {
            if (target instanceof TameableEntity tameableEntity && tameableEntity.getOwner() == player) continue;
            logic.accept(player, target);
        }
    }

    public static void playCombatEffect(PlayerEntity player, LivingEntity target, Identifier visual, SoundEvent sound) {
        CombatUtils.playImpactVisual(player, target, visual);
        SoundUtils.playSound(player, sound);
    }

    public static void playCombatEffects(PlayerEntity player, LivingEntity target, List<Identifier> visuals, List<SoundEvent> sounds) {
        for (Identifier visual: visuals) {
            CombatUtils.playImpactVisual(player, target, visual);
        }
        for (SoundEvent sound: sounds) {
            SoundUtils.playSound(player, sound);
        }
    }

    public static void knockback(PlayerEntity player, LivingEntity target, Vec3d knockbackMultiplier) {
        target.setVelocity(player.getRotationVector().x * knockbackMultiplier.x,  1 * knockbackMultiplier.y, player.getRotationVector().z * knockbackMultiplier.z);
        target.velocityModified = true;
    }
}
