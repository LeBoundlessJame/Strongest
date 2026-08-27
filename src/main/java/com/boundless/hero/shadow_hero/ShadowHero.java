package com.boundless.hero.shadow_hero;

import com.boundless.BoundlessAPI;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.black_sparks_hero.BrawlerHUD;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.registry.AttributeRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

import static com.boundless.registry.DataComponentRegistry.registerComponent;

public class ShadowHero extends Hero {
    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.id("generic_max_health"), 380f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.id("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.id("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.id("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.id("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public ShadowHero() {
        TenShadowsComponents.initialize();

        TechniqueLoadout loadout = TechniqueLoadout.builder()
                .ability(AbilityKey.ATTACK, TenShadowsTechnique.PUNCH)
                .ability(AbilityKey.USE, TenShadowsTechnique::getRightClickAbility)
                .ability(AbilityKey.ABILITY_TWO, TenShadowsTechnique.SHIRO)
                .ability(AbilityKey.ABILITY_THREE, TenShadowsTechnique.GAMA)
                .ability(AbilityKey.ABILITY_ONE, TenShadowsTechnique.KURO)
                .ability(AbilityKey.ABILITY_FOUR, TenShadowsTechnique.SHIKIGAMI_ORDERS)
                .build();

        this.heroData = HeroData.builder()
                .name("shadow_hero")
                .defaultTechniqueLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("shadow_hero"))
                .textureIdentifier(BoundlessAPI.textureID("shadow_hero"))
                .tickHandler(Hero::onHeroTick)
                .maxCursedEnergy(5000)
                .build();
        this.registerHero();
    }
}
