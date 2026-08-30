package com.boundless.mechanics;

import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

public class BlackFlashManager {

    public static boolean shouldBlackFlash(PlayerEntity player) {
        return player.getRandom().nextFloat() < getBlackFlashChance(player);
    }

    public static float getBlackFlashChance(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_CHANCE, 0.0f);
    }

    public static float getBlackFlashMultiplier(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_DAMAGE_MULTIPLIER, 2.5f);
    }

    public static void playBlackFlashVisuals(LivingEntity entity, int duration) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, duration - 2, 4, true, false, false));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, duration, 4, true, false, false));
    }
}
