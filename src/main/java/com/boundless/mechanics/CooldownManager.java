package com.boundless.mechanics;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.ComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class CooldownManager {
    public static void setAbilityCooldownIfHigher(LivingEntity user, Identifier abilityID, long cooldownTime) {
        if (getRemainingCooldownTicks(user, abilityID) > cooldownTime) return;

        ItemStack heroStack = HeroUtils.getHeroStack(user);
        ComponentUtils.updateMap(heroStack, DataComponentRegistry.COOLDOWN_DATA, abilityID, user.getWorld().getTime() + cooldownTime);
    }

    public static void setAbilityCooldown(LivingEntity user, Identifier abilityID, long cooldownTime) {
        ItemStack heroStack = HeroUtils.getHeroStack(user);
        ComponentUtils.updateMap(heroStack, DataComponentRegistry.COOLDOWN_DATA, abilityID, user.getWorld().getTime() + cooldownTime);
    }

    public static long getAbilityCooldownEnd(LivingEntity user, Identifier abilityId) {
        ItemStack heroStack = HeroUtils.getHeroStack(user);
        Map<Identifier, Long> cooldownData = heroStack.getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
        return cooldownData.getOrDefault(abilityId, 0L);
    }

    public static long getRemainingCooldownTicks(LivingEntity user, Identifier abilityId) {
        return getAbilityCooldownEnd(user, abilityId) - user.getWorld().getTime();
    }

    public static boolean isOnCooldown(LivingEntity user, Identifier abilityId) {
        return user.getWorld().getTime() < getAbilityCooldownEnd(user, abilityId);
    }
}
