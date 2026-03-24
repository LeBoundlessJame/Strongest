package com.boundless.util;

import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StrongestComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

public class AbilityUtils {
    public static void setAbilityCooldown(PlayerEntity player, Identifier abilityID, long cooldownTime) {
        ItemStack heroStack = player.getEquippedStack(EquipmentSlot.CHEST);
        Map<Identifier, Long> updatedCooldownData = ComponentUtils.updatedCooldownMap(heroStack, abilityID, player.getWorld().getTime() + cooldownTime);
        heroStack.set(DataComponentRegistry.COOLDOWN_DATA, updatedCooldownData);
    }

    public static Long getRemainingCooldown(PlayerEntity player, Identifier abilityID) {
        ItemStack heroStack = player.getEquippedStack(EquipmentSlot.CHEST);
        Map<Identifier, Long> cooldownData = heroStack.get(DataComponentRegistry.COOLDOWN_DATA);
        if (cooldownData == null || cooldownData.get(abilityID) == null) return null;
        return cooldownData.get(abilityID) - player.getWorld().getTime();
    }

    public static boolean isOnCooldown(PlayerEntity player, Identifier abilityID) {
        Long remaining = getRemainingCooldown(player, abilityID);
        if (remaining == null) return false;
        return remaining > 0;
    }

    public static Identifier abilityIDFromKeybind(PlayerEntity player, String keybindTranslation) {
        if (!HeroUtils.isHero(player)) return null;
        ItemStack stack = HeroUtils.getHeroStack(player);
        Map<String, Identifier> abilities = stack.getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, Map.of());
        return abilities.get(keybindTranslation);
    }

    public static boolean checkAndUseAbility(PlayerEntity player, Identifier abilityID) {
        if (!HeroUtils.isHero(player)) return false;

        Ability ability = AbilityRegistry.getAbilityFromID(abilityID);
        if (ability == null) return false;

        if (ability.canUseAbility(player)) {
            ability.use(player);
            return true;
        }

        return false;
    }

    public static void setNextAbilityUseTime(PlayerEntity player, long duration) {
        HeroUtils.getHeroStack(player).set(StrongestComponents.NEXT_ABILITY_USE, player.getWorld().getTime() + duration);
    }

    public static boolean nextAbilityUsable(PlayerEntity player) {
        return player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.NEXT_ABILITY_USE, player.getWorld().getTime());
    }
}