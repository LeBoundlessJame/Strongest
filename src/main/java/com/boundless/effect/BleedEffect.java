package com.boundless.effect;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

public class BleedEffect extends StatusEffect {
    private int damageInterval = 20;
    private float damagePerLevel = 3f;

    public BleedEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.age % this.damageInterval != 0) return true;
        EffekUtils.playVisual(entity, BoundlessAPI.identifier("dismantle_impact"), new Vec3d(entity.getHeight() / 16, entity.getHeight() / 16, entity.getHeight() / 16));
        entity.damage(DamageTypeRegistry.getDamageSource(entity, DamageTypeRegistry.BYPASS_DEFENCE), (amplifier + 1) * this.damagePerLevel);
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
