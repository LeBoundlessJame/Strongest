package com.boundless.mechanics;

import com.boundless.registry.StrongestComponents;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class BlackFlashManager {
    public static boolean shouldBlackFlash(PlayerEntity player) {
        return player.getRandom().nextFloat() < getBlackFlashMultiplier(player);
    }

    public static float getBlackFlashChance(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_CHANCE, 0.0f);
    }

    public static float getBlackFlashMultiplier(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_DAMAGE_MULTIPLIER, 2.5f);
    }
}
