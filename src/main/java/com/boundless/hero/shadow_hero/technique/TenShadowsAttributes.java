package com.boundless.hero.shadow_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.registry.AttributeRegistry;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

public class TenShadowsAttributes {
    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.id("generic_max_health"), 380f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.id("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.id("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.id("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.id("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();
}
