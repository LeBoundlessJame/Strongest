package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.MeleeUtils;
import com.boundless.util.VFXUtils;
import net.minecraft.entity.player.PlayerEntity;

public class SimpleDomain {
    public static void simpleDomainTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!isSimpleDomainActive(player)) {
            setSimpleDomainTicks(player, 0);
            return;
        }

        MeleeUtils.disorientWithExemption(player, 2, BoundlessAPI.identifier("simple_domain"));
        reduceSimpleDomainHealth(player, 20);
        setSimpleDomainTicks(player,getSimpleDomainTicks(player) + 1);
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

    public static void setSimpleDomainTicks(PlayerEntity player, int value) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.SIMPLE_DOMAIN_TICKS, value);
    }

    public static int getSimpleDomainTicks(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.SIMPLE_DOMAIN_TICKS, 0);
    }

    public static void toggleSimpleDomain(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        DataComponentUtils.toggleBoolean(player, StrongestComponents.SIMPLE_DOMAIN_ACTIVE);
        if (!isSimpleDomainActive(player)) {
            VFXUtils.destroyEffectInstance("simple_domain", player.getId());
        } else {
            VFXUtils.createAndSpawnEffectInstance(player, "simple_domain", null, null, true);
        }
    }
}
