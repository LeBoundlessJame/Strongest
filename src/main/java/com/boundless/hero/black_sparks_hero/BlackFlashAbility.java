package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.switcher_hero.SwitcherHero;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackFlashAbility {
    public static void blackFlash(PlayerEntity player, float damage, Vec3d knockbackMultiplier, HeroActionEntity heroAction) {
        SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
        SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);
        SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_3);
        SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_HEAVY);
        CameraUtils.playCameraShake(player);

        CombatUtils.perEnemyLogic(heroAction, (attacker, livingEntity) -> {
            playBlackFlashVisuals(attacker, 5);

            livingEntity.timeUntilRegen = 0;
            MeleeUtils.knockback(player, livingEntity, knockbackMultiplier);
            CombatUtils.attack(heroAction, damage, Optional.of(BoundlessAPI.identifier("flash")));
            if (livingEntity instanceof PlayerEntity target) {
                playBlackFlashVisuals(target, 6);
            }
        });

        AttackUtils.startAttackTimer(player, 10);
        resetBlackFlashChance(player);
    }

    public static void resetBlackFlashChance(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.BLACK_FLASH_CHANCE, 0.01f);
    }

    public static float getBlackFlashChance(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_CHANCE, 0.01f);
    }

    public static boolean calculateBlackFlash(PlayerEntity player) {
        return player.getRandom().nextBetween(0, 1) / 100f >= 1 - BlackFlashAbility.getBlackFlashChance(player);
    }

    public static void playBlackFlashVisuals(LivingEntity entity, int duration) {
        // default = 6
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, duration - 2, 4, true, false, false));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, duration, 4, true, false, false));

    }
}
