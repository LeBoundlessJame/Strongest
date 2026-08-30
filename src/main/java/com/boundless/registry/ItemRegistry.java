package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.item.FullHealItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ItemRegistry {
    public static ArrayList<Item> ITEMS = new ArrayList<>();

    public static final Item CLEAVER = registerItem(BoundlessAPI.id("cleaver"), new Item(new Item.Settings()));
    public static final Item PLAYFUL_CLOUD = registerItem(BoundlessAPI.id("playful_cloud"), new Item(new Item.Settings().maxCount(1)));
    // Todo: for testing only. Remove eventually
    public static final Item FULL_HEAL = registerItem(BoundlessAPI.id("full_heal"), new FullHealItem(new Item.Settings()));

    public static Item registerItem(Identifier identifier, Item item) {
        return Registry.register(Registries.ITEM, identifier, item);
    }

    // Todo: make it so that registerItem automatically adds to the array
    public static void initialize() {
        ITEMS.add(CLEAVER);
        ITEMS.add(PLAYFUL_CLOUD);
        ITEMS.add(FULL_HEAL);
    }
}
