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

    public static final Model MASK_LARGE = item("mask_large");
    public static final Model CHESTPLATE_LARGE = item("chestplate_large");
    public static final Model LEGGINGS_LARGE = item("leggings_large");
    public static final Model BOOTS_LARGE = item("boots_large");

    private static Model item(String parent) {
        return new Model(Optional.of(Identifier.of(BoundlessAPI.MOD_ID, "item/" + parent)), null, TextureKey.LAYER0);
    }

    public static TextureMap heroTexture(Identifier heroTexture) {
        return TextureMap.of(TextureKey.LAYER0, Identifier.of(heroTexture.toString().replace("textures/", "").replace(".png", "")));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Todo: this is a small fix just for the update, change this in the future to not be hard coded
        // Todo: i might write a function later that allows for custom texture sizes to be specified; could be a good fix
        for (Hero hero : HeroRegistry.HEROES) {
            ArrayList<Item> armor = hero.getArmorSet();

            if (hero.heroData.getName().equals("shrine_hero") || hero.heroData.getName().equals("black_sparks_hero")) {
                MASK_LARGE.upload(ModelIds.getItemModelId(armor.get(0)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                CHESTPLATE_LARGE.upload(ModelIds.getItemModelId(armor.get(1)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                LEGGINGS_LARGE.upload(ModelIds.getItemModelId(armor.get(2)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                BOOTS_LARGE.upload(ModelIds.getItemModelId(armor.get(3)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
            } else {
                MASK.upload(ModelIds.getItemModelId(armor.get(0)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                CHESTPLATE.upload(ModelIds.getItemModelId(armor.get(1)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                LEGGINGS.upload(ModelIds.getItemModelId(armor.get(2)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
                BOOTS.upload(ModelIds.getItemModelId(armor.get(3)), heroTexture(hero.heroData.getTextureIdentifier()), itemModelGenerator.writer);
            }
        }
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }
}
