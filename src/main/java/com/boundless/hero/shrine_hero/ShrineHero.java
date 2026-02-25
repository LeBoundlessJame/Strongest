package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.black_sparks_hero.BrawlerHUD;
import com.boundless.hero.switcher_hero.SwitcherConfig;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import static com.boundless.hero.shrine_hero.ShrineHeroMelee.LIGHT_ATTACK;

public class ShrineHero extends Hero {
    public static ComponentType<Integer> FINGER_COUNT = DataComponentRegistry.registerComponent("finger_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public static ShrineConfig CONFIG = ConfigRegistry.HERO_CONFIG.SHRINE_CONFIG;
    public static ShrineConfig.AbilityDamageConfig DAMAGE = CONFIG.ABILITY_DAMAGE_CONFIG;
    public static ShrineConfig.AbilityCooldownConfig COOLDOWNS = CONFIG.ABILITY_COOLDOWN_CONFIG;
    public static ShrineConfig.DomainConfig DOMAIN = CONFIG.DOMAIN_CONFIG;

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 40f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), ShrineHero.CONFIG.damageReduction.get(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 65, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 3.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public ShrineHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", LIGHT_ATTACK)
                .ability("key.use", ShrineHeroMelee.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", ShrineHeroSlashes.DISMANTLE)
                .ability("key.boundless.ability_two", ShrineHeroSlashes.CLEAVE)
                .ability("key.boundless.ability_three", ShrineHeroDestruction.OPEN)
                .ability("key.boundless.ability_five", ShrineHeroDestruction.SHRINE)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("shrine_hero")
                .modelIdentifier(BoundlessAPI.modelID("brawler"))
                .textureIdentifier(BoundlessAPI.textureID("shrine_hero"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(HeroArmorRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .build();
        this.registerHero();
    }
}