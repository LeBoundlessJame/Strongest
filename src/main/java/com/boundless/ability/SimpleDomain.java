package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.player.PlayerEntity;

public class SimpleDomain {
    public static void simpleDomainTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!isSimpleDomainActive(player)) return;

        MeleeUtils.disorientWithExemption(player, 2, BoundlessAPI.identifier("simple_domain"));
        //DataComponentUtils.incrementInt(StrongestComponents.SIMPLE_DOMAIN_HEALTH, player, -1, 0, 150);
    }

    public static boolean isSimpleDomainActive(PlayerEntity player) {
        return DataComponentUtils.getBoolean(player, StrongestComponents.SIMPLE_DOMAIN_ACTIVE);
    }

    public static void toggleSimpleDomain(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        if (DataComponentUtils.toggleBoolean(player, StrongestComponents.SIMPLE_DOMAIN_ACTIVE)) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("simple_domain"));
            VFXUtils.createAndSpawnEffectInstance(player, "simple_domain", null, null, true);
            HeroUtils.getHeroStack(player).set(StrongestComponents.SIMPLE_DOMAIN_HEALTH, 150);
            return;
        }

        VFXUtils.destroyEffectInstance("simple_domain", player.getId());
    }
}
