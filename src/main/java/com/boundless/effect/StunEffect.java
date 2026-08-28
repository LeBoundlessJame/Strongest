package com.boundless.effect;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

public class StunEffect extends StatusEffect {
    public StunEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.age % 100 != 0) return true;
        EffekUtils.playVisual(entity, BoundlessAPI.id("stun"), new Vec3d(entity.getHeight() / 16, entity.getHeight() / 16, entity.getHeight() / 16));
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
