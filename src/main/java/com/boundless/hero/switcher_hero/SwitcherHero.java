package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.black_sparks_hero.BlackSparksHeroConfig;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class SwitcherHero extends Hero {
    public static SwitcherConfig CONFIG = ConfigRegistry.HERO_CONFIG.SWITCHER_CONFIG;
    public static SwitcherConfig.AbilityDamageConfig DAMAGE = CONFIG.abilityDamageConfig;
    public static SwitcherConfig.AbilityCooldownConfig COOLDOWNS = CONFIG.abilityCooldownConfig;

    public static Ability LIGHT_ATTACK = AbilityUtils.ability(SwitcherLightLogic::lightAttack, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("switcher_light_attack"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(SwitcherMediumLogic::mediumAttack, COOLDOWNS.mediumAttack.get(), BoundlessAPI.identifier("todo_medium"), BoundlessAPI.hudPNG("leg"));

    public static Ability BOOGIE = AbilityUtils.ability(BoogieLogic::clap, COOLDOWNS.clap.get(), BoundlessAPI.identifier("boogie"), BoundlessAPI.hudPNG("clap"));
    public static Ability ROCK_THROW = AbilityUtils.ability(RockThrowLogic::rockThrow, COOLDOWNS.rockThrow.get(), BoundlessAPI.identifier("rock_throw"), BoundlessAPI.hudPNG("rock_throw"));
    public static Ability TARGET_SELECT = AbilityUtils.ability(TargetSelectMenu::openTargetSelectMenu, COOLDOWNS.targetSelect.get(), BoundlessAPI.identifier("boogie_menu"), BoundlessAPI.hudPNG("select_target"));

    public static ComponentType<Long> LAST_REVIVE_TIMESTAMP = DataComponentRegistry.registerComponent("last_revive_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> TIME_UNTIL_NEXT_REVIVE = DataComponentRegistry.registerComponent("time_until_next_revive", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> CLAP_SELECT_TIME = DataComponentRegistry.registerComponent("clap_select_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> TARGET_SELECT_TIME = DataComponentRegistry.registerComponent("target_select_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<String> BOOGIE_SELECTION = DataComponentRegistry.registerComponent("boogie_selection", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static ComponentType<Integer> PRIMARY_TARGET_ID = DataComponentRegistry.registerComponent("primary_target_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> SECONDARY_TARGET_ID = DataComponentRegistry.registerComponent("secondary_target_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.75, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SCALE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_scale"), 0.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .build();

    public SwitcherHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", SwitcherHero.LIGHT_ATTACK)
                .ability("key.use", SwitcherHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", SwitcherHero.BOOGIE)
                .ability("key.boundless.ability_two", SwitcherHero.ROCK_THROW)
                .ability("key.boundless.ability_three", SwitcherHero.TARGET_SELECT)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("switcher_hero")
                .textureIdentifier(BoundlessAPI.textureID("switcher"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BoogieHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(HeroArmorRenderer::new)
                .armorRenderer(SwitcherRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .modelIdentifier(BoundlessAPI.modelID("switcher"))
                .build();
        this.registerHero();
    }
}
