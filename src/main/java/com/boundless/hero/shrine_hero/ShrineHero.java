package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.black_sparks_hero.BlackSparksHUD;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class ShrineHero extends Hero {
    public static ComponentType<Long> CHARGED_LEAP_TIME_WINDOW = DataComponentRegistry.registerComponent("charged_leap_time_window", builder -> ComponentType.<Long>builder().codec(Codec.LONG));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 40f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.8f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 65, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 3.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public ShrineHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", com.boundless.hero.black_sparks_hero.BlackSparksHero.LIGHT_ATTACK)
                .ability("key.use", com.boundless.hero.black_sparks_hero.BlackSparksHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", com.boundless.hero.black_sparks_hero.BlackSparksHero.DASH)
                .ability("key.boundless.ability_two", com.boundless.hero.black_sparks_hero.BlackSparksHero.SPIN_KICK)
                .ability("key.boundless.ability_three", com.boundless.hero.black_sparks_hero.BlackSparksHero.BLACK_FLASH)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("shrine_hero")
                .textureIdentifier(BoundlessAPI.textureID("shrine_hero"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BlackSparksHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(HeroArmorRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .build();
        this.registerHero();
    }
}