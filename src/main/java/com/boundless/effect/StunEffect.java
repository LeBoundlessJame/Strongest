package com.boundless.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class StunEffect extends StatusEffect {
    public StunEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        // apply bound CUSTOM effect instance
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        // remove custom effect instance from world
    }
}
