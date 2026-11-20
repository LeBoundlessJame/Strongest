package com.boundless.util;

import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

// Todo: this whole class could do with an expansion / elaboration
public class ComponentUtils {

    // Todo: change to use user instead of itemstack directly
    public static <A, B> void updateMap(ItemStack heroStack, ComponentType<Map<A, B>> mapComponent, A key, B value) {
        HashMap<A, B> map = new HashMap<>(heroStack.getOrDefault(mapComponent, new HashMap<>()));
        map.put(key, value);
        heroStack.set(mapComponent, map);
    }

    public static void incrementInt(ComponentType<Integer> component, LivingEntity user, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(user);
        stack.set(component, stack.getOrDefault(component, 0) + amount);
    }

    public static void toggleBoolean(LivingEntity user, ComponentType<Boolean> component, boolean fallback) {
        ItemStack stack = HeroUtils.getHeroStack(user);
        stack.set(component, !stack.getOrDefault(component, fallback));
    }

    public static <T> void set(LivingEntity user, ComponentType<T> component, T value) {
        HeroUtils.getHeroStack(user).set(component, value);
    }

    public static <T> void remove(LivingEntity user, ComponentType<T> component) {
        HeroUtils.getHeroStack(user).remove(component);
    }

    public static <T> T get(LivingEntity user, ComponentType<T> component) {
        return HeroUtils.getHeroStack(user).get(component);
    }

    public static <T> T getOr(LivingEntity user, ComponentType<T> component, T fallback) {
        return HeroUtils.getHeroStack(user).getOrDefault(component, fallback);
    }
}
