package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.armor.HeroArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ArmorRenderRegistry {

    public static void initialize() {
        for (Hero hero: HeroRegistry.HEROES) {
            Identifier texture = hero.heroData.getTextureIdentifier();
            Identifier model = hero.heroData.getModelIdentifier();
            ArrayList<Item> armorSet = hero.getArmorSet();
            AzArmorRendererRegistry.register(() -> new HeroArmorRenderer(model, texture), armorSet.getFirst(), armorSet.get(1), armorSet.get(2), armorSet.getLast());
        }
    }
}
