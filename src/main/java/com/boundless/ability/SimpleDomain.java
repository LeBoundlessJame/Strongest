package com.boundless.ability;

import com.boundless.hero.api.Hero;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.HeroUtils;
import com.boundless.util.MeleeUtils;
import com.boundless.util.VFXUtils;
import net.minecraft.entity.player.PlayerEntity;

public class SimpleDomain {
    public static void simpleDomainTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!isSimpleDomainActive(player)) return;

        int simpleDomainTicks = getSimpleDomainTicks(player);

        if (simpleDomainTicks == 1) {
            VFXUtils.createAndSpawnEffectInstance(player, "simple_domain", null, null, true);
        } else if (simpleDomainTicks <= 0) {
            VFXUtils.destroyEffectInstance("simple_domain", player.getId());
        }

        MeleeUtils.disorient(player, 2);
        reduceSimpleDomainHealth(player, 20);
    }

    public static void reduceSimpleDomainHealth(PlayerEntity player, int amount) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.SIMPLE_DOMAIN_HEALTH, Math.clamp(getSimpleDomainHealth(player) - amount, 0, 150));
    }

    public static int getSimpleDomainHealth(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.SIMPLE_DOMAIN_HEALTH, 150);
    }

    public static boolean isSimpleDomainActive(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.SIMPLE_DOMAIN_ACTIVE, false);
    }

    public static int getSimpleDomainTicks(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.SIMPLE_DOMAIN_TICKS, 0);
    }

    public static void toggleSimpleDomain(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.SIMPLE_DOMAIN_ACTIVE, !HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.SIMPLE_DOMAIN_ACTIVE, false));
    }
}
