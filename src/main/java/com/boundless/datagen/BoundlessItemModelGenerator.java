package com.boundless.datagen;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.registry.HeroRegistry;
import com.boundless.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Optional;

public class BoundlessItemModelGenerator extends FabricModelProvider {
    public BoundlessItemModelGenerator(FabricDataOutput output) {
        super(output);
    }

    public static final Model MASK = item("mask");
    public static final Model CHESTPLATE = item("chestplate");
    public static final Model LEGGINGS = item("leggings");
    public static final Model BOOTS = item("boots");


    private static Model item(String parent) {
        return new Model(Optional.of(Identifier.of(BoundlessAPI.MOD_ID, "item/" + parent)), null, TextureKey.LAYER0);
    }

    public static TextureMap heroTexture(Identifier heroTexture) {
        return TextureMap.of(TextureKey.LAYER0, Identifier.of(heroTexture.toString().replace("textures/", "").replace(".png", "")));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemRegistry.DEMONIC_FINGER, Models.GENERATED);

        for (Hero hero : HeroRegistry.HEROES) {
            ArrayList<Item> armor = hero.getArmorSet();
            MASK.upload(ModelIds.getItemModelId(armor.get(0)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
            CHESTPLATE.upload(ModelIds.getItemModelId(armor.get(1)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
            LEGGINGS.upload(ModelIds.getItemModelId(armor.get(2)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
            BOOTS.upload(ModelIds.getItemModelId(armor.get(3)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
        }
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }
}
