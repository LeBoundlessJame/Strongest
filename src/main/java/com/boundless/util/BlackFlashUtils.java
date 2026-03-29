package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.black_sparks_hero.BlackFlashAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class BlackFlashUtils {
    public static void blackFlash(PlayerEntity player, float damage, Vec3d knockbackMultiplier, HeroActionEntity action) {
        SoundUtils.playSound(player, player.age % 2 == 0 ? SoundRegistry.PUNCH_1 : SoundRegistry.PUNCH_2, 9, 11);
        SoundUtils.playSounds(player, List.of(SoundRegistry.EARTH_IMPACT, SoundRegistry.ENERGY_IMPACT_2, SoundRegistry.ENERGY_IMPACT_3, SoundRegistry.ENERGY_IMPACT_HEAVY));

        CameraUtils.playCameraShake(player);

        MeleeUtils.forEach(player, action, (user, entity) -> {
            entity.timeUntilRegen = 0;
            entity.damage(entity.getDamageSources().generic(), damage);

            if (entity instanceof PlayerEntity playerEntity) {
                playBlackFlashVisuals(playerEntity, 5);
                CameraUtils.playCameraShake(playerEntity);
            }

            if (entity instanceof LivingEntity livingEntity) {
                CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("flash"));
                MeleeUtils.knockback(user, livingEntity, knockbackMultiplier);
            }
        });

        resetBlackFlashChance(player);
    }

    public static void resetBlackFlashChance(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.BLACK_FLASH_CHANCE, 0.01f);
    }

    public static float getBlackFlashChance(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_CHANCE, 0.01f);
    }

    public static boolean isBlackFlashHit(PlayerEntity player) {
        return player.getRandom().nextBetween(0, 1) / 100f >= 1 - BlackFlashAbility.getBlackFlashChance(player);
    }

    public static void playBlackFlashVisuals(LivingEntity entity, int duration) {
        // default = 6
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, duration - 2, 4, true, false, false));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, duration, 4, true, false, false));

    }
}
