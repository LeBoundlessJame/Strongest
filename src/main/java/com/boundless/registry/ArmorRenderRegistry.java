package com.boundless.registry;

import com.boundless.hero.api.Hero;
import com.boundless.hero.armor.HeroArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class ArmorRenderRegistry {

    public static void initialize() {
        for (Hero hero: HeroRegistry.HEROES) {
            ArrayList<Item> armorSet = hero.getArmorSet();
            BiFunction<Identifier, Identifier, ? extends AzArmorRenderer> armorRenderer = hero.heroData.getArmorRenderer();
            AzArmorRendererRegistry.register(() -> armorRenderer.apply(hero.heroData.getModelIdentifier(), hero.heroData.getTextureIdentifier()), armorSet.getFirst(), armorSet.get(1), armorSet.get(2), armorSet.getLast());
        }
    }
}
