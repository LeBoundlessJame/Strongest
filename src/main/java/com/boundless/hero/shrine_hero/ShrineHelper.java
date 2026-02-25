package com.boundless.hero.shrine_hero;

import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Math;

public class ShrineHelper {
    public static float getScaledDamage(PlayerEntity player, float min, float max) {
        float progress = Math.clamp(0, 1, (float) getFingerCount(player) / 15);
        return Math.lerp(min, max, progress);
    }

    public static int getFingerCount(PlayerEntity player) {
        if (!HeroUtils.isHero(player)) return 0;
        return HeroUtils.getHeroStack(player).getOrDefault(ShrineHero.FINGER_COUNT, 0);
    }
}
