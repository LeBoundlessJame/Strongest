package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.item.DemonicFingerItem;
import com.boundless.item.FullHealItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ItemRegistry {
    public static ArrayList<Item> ITEMS = new ArrayList<>();

    public static final Item PLAYFUL_CLOUD = registerItem(BoundlessAPI.identifier("playful_cloud"), new Item(new Item.Settings().maxCount(1)));
    public static final Item DEMONIC_FINGER = registerItem(BoundlessAPI.identifier("demonic_finger"), new DemonicFingerItem(new Item.Settings().food(new FoodComponent.Builder().snack().alwaysEdible().build()).maxCount(20)));
    // Todo: for testing only. Remove eventually
    public static final Item FULL_HEAL = registerItem(BoundlessAPI.identifier("full_heal"), new FullHealItem(new Item.Settings()));

    public static Item registerItem(Identifier identifier, Item item) {
        return Registry.register(Registries.ITEM, identifier, item);
    }

    // Todo: make it so that registerItem automatically adds to the array
    public static void initialize() {
        ITEMS.add(PLAYFUL_CLOUD);
        ITEMS.add(DEMONIC_FINGER);
        ITEMS.add(FULL_HEAL);
    }
}
