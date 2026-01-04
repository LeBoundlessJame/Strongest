package com.boundless.util;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroArmor;
import com.boundless.hero.api.HeroData;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class RegistryUtils {
    public static ArrayList<Item> registerHero(Hero hero) {
        return registerHero(hero.getHeroData());
    }

    public static ArrayList<Item> registerHero(HeroData heroData) {
        String name = heroData.getName();

        ArrayList<Item> heroArmor = new ArrayList<Item>();
        heroArmor.add(armorPiece(name + "_helmet", ArmorItem.Type.HELMET, heroData));
        heroArmor.add(armorPiece(name + "_chestplate", ArmorItem.Type.CHESTPLATE, heroData));
        heroArmor.add(armorPiece(name + "_leggings",  ArmorItem.Type.LEGGINGS, heroData));
        heroArmor.add(armorPiece(name + "_boots", ArmorItem.Type.BOOTS, heroData));
        return heroArmor;
    }

    public static Item armorPiece(String name, ArmorItem.Type armorSlot, HeroData heroData) {
        HeroArmor item = new HeroArmor(ArmorMaterials.LEATHER, armorSlot, new Item.Settings().maxCount(1), heroData);
        return registerItem(item, name);
    }

    public static Item registerItem(Item instance, String path) {
        return Registry.register(Registries.ITEM, Identifier.of(BoundlessAPI.MOD_ID, path), instance);
    }
}
