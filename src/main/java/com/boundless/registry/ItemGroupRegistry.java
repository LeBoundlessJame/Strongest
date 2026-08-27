package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class ItemGroupRegistry {

    public static final ItemGroup BOUNDLESS_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(HeroRegistry.BLACK_SPARKS_HERO.getArmorSet().getFirst()))
            .displayName(Text.translatable("itemGroup.boundless.boundless_group"))
            .entries((context, entries) -> {
                for (Hero hero: HeroRegistry.HEROES) {
                    entries.add(hero.getArmorSet().getFirst());
                    entries.add(hero.getArmorSet().get(1));
                    entries.add(hero.getArmorSet().get(2));
                    entries.add(hero.getArmorSet().getLast());
                }

                for (Item item: ItemRegistry.ITEMS) {
                    entries.add(item);
                }
            })
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, BoundlessAPI.id("boundless_group"), BOUNDLESS_GROUP);
    }
}
