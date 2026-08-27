package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.BasicAbilities;
import com.boundless.registry.AttributeRegistry;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class SwitcherStats {
    public static AbilityLoadout LOADOUT_1 = AbilityLoadout.builder()
            .ability("key.attack", SwitcherHero.LIGHT_ATTACK)
            .ability("key.use", SwitcherHero.MEDIUM_ATTACK)
            .ability("key.boundless.ability_one", SwitcherHero.BOOGIE)
            .ability("key.boundless.ability_two", SwitcherHero.ROCK_THROW)
            .ability("key.boundless.ability_three", SwitcherHero.TARGET_SELECT)
            .ability("key.boundless.ability_four", SwitcherHero.SUPLEX)
            .ability("key.boundless.combat_mode_toggle", BasicAbilities.COMBAT_MODE_TOGGLE)
            .build();

    public static AbilityLoadout LOADOUT_2 = AbilityLoadout.builder()
            .ability("key.attack", SwitcherHero.LIGHT_ATTACK)
            .ability("key.use", SwitcherHero.MEDIUM_ATTACK)
            .ability("key.boundless.ability_one", SwitcherHero.BOOGIE)
            .ability("key.boundless.ability_two", SwitcherHero.SWAP_WITH_PRIMARY)
            .ability("key.boundless.ability_three", SwitcherHero.SWAP_WITH_SECONDARY)
            .ability("key.boundless.ability_four", SwitcherHero.SWAP_TWO)
            .ability("key.boundless.combat_mode_toggle", BasicAbilities.COMBAT_MODE_TOGGLE)
            .build();

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.id("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.id("damage_resistance"), SwitcherHero.CONFIG.damageReduction.get(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.id("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.id("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.id("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.id("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SCALE, new EntityAttributeModifier(BoundlessAPI.id("generic_scale"), 0.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .build();
}
