package com.boundless.datagen;

import com.boundless.hero.api.Hero;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.registry.HeroRegistry;
import com.boundless.registry.ItemRegistry;
import com.boundless.registry.StatusEffectRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BoundlessTranslationGenerator extends FabricLanguageProvider {
    public BoundlessTranslationGenerator(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataGenerator, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(StatusEffectRegistry.INVULNERABILITY_EFFECT.value(), "Invulnerability");

        translationBuilder.add(ItemRegistry.DEMONIC_FINGER, "Demonic Finger");
        translationBuilder.add(ItemRegistry.PLAYFUL_CLOUD, "Playful Cloud");

        translationBuilder.add(DamageTypeRegistry.SHRINE_SLASHES.getRegistry(), "Shrine Slashes");
        translationBuilder.add(DamageTypeRegistry.CURSED_ENERGY.getRegistry(), "Cursed Energy");
        translationBuilder.add(DamageTypeRegistry.BYPASS_DEFENCE.getRegistry(), "Defence Bypass");

        translationBuilder.add("itemGroup.boundless.boundless_group", "Boundless");
        translationBuilder.add("key.boundless.ability_one", "Ability 1");
        translationBuilder.add("key.boundless.ability_two", "Ability 2");
        translationBuilder.add("key.boundless.ability_three", "Ability 3");
        translationBuilder.add("key.boundless.ability_four", "Ability 4");
        translationBuilder.add("key.boundless.ability_five", "Ability 5");
        translationBuilder.add("category.boundless.controls", "Boundless Controls");
        translationBuilder.add("boundless.damage_resistance", "Damage Resistance");

        for (Hero hero : HeroRegistry.HEROES) {
            ArrayList<Item> armor = hero.getArmorSet();
            String displayName = hero.heroData.getDisplayName();
            if (displayName.equals("hero")) {
                displayName = hero.heroData.getName().replace("_", " ");
            }

            translationBuilder.add(armor.get(0), StringUtils.capitalize(displayName) + "'s " + "Helmet");
            translationBuilder.add(armor.get(1), StringUtils.capitalize(displayName) + "'s " + "Chestplate");
            translationBuilder.add(armor.get(2), StringUtils.capitalize(displayName) + "'s " + "Leggings");
            translationBuilder.add(armor.get(3), StringUtils.capitalize(displayName) + "'s " + "Boots");
        }
    }
}