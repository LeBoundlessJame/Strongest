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
    public static <A, B> void updateMap(ItemStack heroStack, ComponentType<Map<A, B>> mapComponent, A key, B value) {
        HashMap<A, B> map = new HashMap<>(heroStack.getOrDefault(mapComponent, new HashMap<>()));
        map.put(key, value);
        heroStack.set(mapComponent, map);
    }

    public static void incrementInt(ComponentType<Integer> component, PlayerEntity player, int amount) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(component, stack.getOrDefault(component, 0) + amount);
    }
}
