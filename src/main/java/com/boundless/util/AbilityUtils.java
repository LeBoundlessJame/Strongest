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
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AbilityUtils {
    public static Ability ability(Consumer<PlayerEntity> abilityLogic, int cooldown, Identifier abilityID, String displayString, int cost) {
        return Ability.builder().abilityLogic(abilityLogic).cooldown(cooldown).abilityID(abilityID).displayString(displayString).cost(cost).build();
    }

    public static Ability ability(Consumer<PlayerEntity> abilityLogic, int cooldown, Identifier abilityID, String displayString, int cost, int skillSlot, Identifier skillSlotTexture) {
        return Ability.builder().abilityLogic(abilityLogic).cooldown(cooldown).abilityID(abilityID).displayString(displayString).cost(cost).skillSlot(skillSlot).skillSlotTexture(skillSlotTexture).build();
    }

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
}