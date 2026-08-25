package com.boundless.datagen;

import com.boundless.hero.api.Hero;
import com.boundless.registry.*;
import com.boundless.util.KeybindingUtils;
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
        translationBuilder.add(EntityRegistry.DIVINE_DOG_KURO, "Kuro");
        translationBuilder.add(EntityRegistry.DIVINE_DOG_SHIRO, "Shiro");
        translationBuilder.add(EntityRegistry.GAMA, "Gama");

        translationBuilder.add(StatusEffectRegistry.INVULNERABILITY_EFFECT.value(), "Invulnerability");
        translationBuilder.add(StatusEffectRegistry.BLEED.value(), "Bleed");

        translationBuilder.add(AttributeRegistry.TOP_SPEED_MULTIPLIER, "Top-Speed Multiplier");
        translationBuilder.add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, "Max-Speed Buildup Time");
        translationBuilder.add(AttributeRegistry.DAMAGE_RESISTANCE, "Damage Resistance");

        translationBuilder.add(ItemRegistry.DEMONIC_FINGER, "Demonic Finger");
        translationBuilder.add(ItemRegistry.PLAYFUL_CLOUD, "Playful Cloud");

        translationBuilder.add("itemGroup.boundless.boundless_group", "Boundless");
        translationBuilder.add(KeybindRegistry.ABILITY_ONE.getTranslationKey(), "Ability 1");
        translationBuilder.add(KeybindRegistry.ABILITY_TWO.getTranslationKey(), "Ability 2");
        translationBuilder.add(KeybindRegistry.ABILITY_THREE.getTranslationKey(), "Ability 3");
        translationBuilder.add(KeybindRegistry.ABILITY_FOUR.getTranslationKey(), "Ability 4");
        translationBuilder.add(KeybindRegistry.ABILITY_FIVE.getTranslationKey(), "Ability 5");
        translationBuilder.add(KeybindRegistry.COMBAT_MODE_TOGGLE.getTranslationKey(), "Combat Mode Toggle");
        translationBuilder.add("category.boundless.controls", "Boundless Controls");

        translationBuilder.add("death.attack.bypass_defence", "%1$s's weaknesses were exploited");
        translationBuilder.add("death.attack.cursed_energy", "%1$s was cursed to death");
        translationBuilder.add("death.attack.shrine_slashes", "%1$s met a malevolent fate");

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