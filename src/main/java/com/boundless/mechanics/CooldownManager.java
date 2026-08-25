package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class CooldownManager {
    public static void setAbilityCooldown(PlayerEntity player, Identifier abilityID, long cooldownTime) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        DataComponentUtils.updateMap(heroStack, DataComponentRegistry.COOLDOWN_DATA, abilityID, player.getWorld().getTime() + cooldownTime);
    }

    public static long getAbilityCooldown(PlayerEntity player, Identifier abilityId) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<Identifier, Long> cooldownData = heroStack.getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
        return cooldownData.getOrDefault(abilityId, 0L);
    }

    public static boolean isOnCooldown(PlayerEntity player, Identifier abilityId) {
        return player.getWorld().getTime() < getAbilityCooldown(player, abilityId);
    }
}
