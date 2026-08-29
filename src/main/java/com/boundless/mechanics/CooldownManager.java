package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class CooldownManager {
    public static void setAbilityCooldownIfHigher(PlayerEntity player, Identifier abilityID, long cooldownTime) {
        if (getRemainingCooldownTicks(player, abilityID) > cooldownTime) return;

        ItemStack heroStack = HeroUtils.getHeroStack(player);
        DataComponentUtils.updateMap(heroStack, DataComponentRegistry.COOLDOWN_DATA, abilityID, player.getWorld().getTime() + cooldownTime);
    }

    public static void setAbilityCooldown(PlayerEntity player, Identifier abilityID, long cooldownTime) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        DataComponentUtils.updateMap(heroStack, DataComponentRegistry.COOLDOWN_DATA, abilityID, player.getWorld().getTime() + cooldownTime);
    }

    public static long getAbilityCooldownEnd(PlayerEntity player, Identifier abilityId) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<Identifier, Long> cooldownData = heroStack.getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
        return cooldownData.getOrDefault(abilityId, 0L);
    }

    public static long getRemainingCooldownTicks(PlayerEntity player, Identifier abilityId) {
        return getAbilityCooldownEnd(player, abilityId) - player.getWorld().getTime();
    }

    public static boolean isOnCooldown(PlayerEntity player, Identifier abilityId) {
        return player.getWorld().getTime() < getAbilityCooldownEnd(player, abilityId);
    }
}
