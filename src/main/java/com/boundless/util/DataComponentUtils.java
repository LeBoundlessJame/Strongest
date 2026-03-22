package com.boundless.util;

import com.boundless.registry.DataComponentRegistry;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

// Todo: this whole class could do with an expansion / elaboration
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

    public static boolean toggleBoolean(PlayerEntity player, ComponentType<Boolean> component) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, !stack.getOrDefault(component, false));
        return stack.getOrDefault(component, false);
    }

    public static boolean getBoolean(PlayerEntity player, ComponentType<Boolean> component) {
        return HeroUtils.getHeroStack(player).getOrDefault(component, false);
    }

    public static void incrementInt(ComponentType<Integer> component, PlayerEntity player, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, stack.getOrDefault(component, 0) + amount);
    }

    public static void incrementInt(ComponentType<Integer> component, PlayerEntity player, int amount, int min, int max) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, Math.clamp(stack.getOrDefault(component, 0) + amount, min, max));
    }

    public static void incrementFloat(ComponentType<Float> component, PlayerEntity player, float amount, float min, float max) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, Math.clamp(stack.getOrDefault(component, 0f) + amount, min, max));
    }
}
