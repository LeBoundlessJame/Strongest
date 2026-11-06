package com.boundless.attribute;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public record AttributeValue(double value, EntityAttributeModifier.Operation operation) {
}
