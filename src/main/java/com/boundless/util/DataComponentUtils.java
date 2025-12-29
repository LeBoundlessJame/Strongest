package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class DataComponentUtils {
    public static Map<Identifier, Long> updatedCooldownMap(ItemStack heroStack, Identifier abilityID, long value) {
        Map<Identifier, Long> cooldownData = heroStack.getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
        Map<Identifier, Long> updatedCooldownData = new HashMap<>(cooldownData);
        updatedCooldownData.put(abilityID, value);
        return updatedCooldownData;
    }

    public static <A, B> void updateMap(ItemStack heroStack, ComponentType<Map<A, B>> mapComponent, A key, B value) {
        HashMap<A, B> map = new HashMap<>(heroStack.getOrDefault(mapComponent, new HashMap<>()));
        map.put(key, value);
        heroStack.set(mapComponent, map);
    }

    public static void toggleBoolean(PlayerEntity player, ComponentType<Boolean> component) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, !stack.getOrDefault(component, false));
    }

    public static int getInt(ComponentType<Integer> component, PlayerEntity player, int defaultValue) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        return stack.getOrDefault(component, defaultValue);
    }

    public static void setInt(ComponentType<Integer> component, PlayerEntity player, int desiredValue) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, desiredValue);
    }

    public static void addOrSubtractInt(ComponentType<Integer> component, PlayerEntity player, int amount, int max) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, Math.clamp(DataComponentUtils.getInt(component, player, 0) + amount, 0, max));
    }

    public static void incrementInt(ComponentType<Integer> component, PlayerEntity player, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, DataComponentUtils.getInt(component, player, 0) + 1);
    }


    public static boolean consumeInt(ComponentType<Integer> component, PlayerEntity player, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        int currentValue = stack.getOrDefault(component, 0);
        boolean hasRequired = stack.getOrDefault(component, 0) >= amount;
        if (hasRequired) {
            stack.set(component, currentValue - amount);
        }
        return hasRequired;
    }
}
