package com.boundless.util;

import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

// Todo: this whole class could do with an expansion / elaboration
public class ComponentUtils {
    public static <A, B> void updateMap(ItemStack heroStack, ComponentType<Map<A, B>> mapComponent, A key, B value) {
        HashMap<A, B> map = new HashMap<>(heroStack.getOrDefault(mapComponent, new HashMap<>()));
        map.put(key, value);
        heroStack.set(mapComponent, map);
    }

    public static void incrementInt(ComponentType<Integer> component, PlayerEntity player, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, stack.getOrDefault(component, 0) + amount);
    }

    public static void toggleBoolean(PlayerEntity player, ComponentType<Boolean> component, boolean fallback) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, !stack.getOrDefault(component, fallback));
    }

    public static <T> void set(PlayerEntity player, ComponentType<T> component, T value) {
        HeroUtils.getHeroStack(player).set(component, value);
    }

    public static <T> void clear(PlayerEntity player, ComponentType<T> component) {
        HeroUtils.getHeroStack(player).remove(component);
    }

    public static <T> T get(PlayerEntity player, ComponentType<T> component) {
        return HeroUtils.getHeroStack(player).get(component);
    }
}
