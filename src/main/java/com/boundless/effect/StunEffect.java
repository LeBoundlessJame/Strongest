package com.boundless.effect;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.util.EffekUtils;
import com.boundless.util.VFXUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;

public class StunEffect extends StatusEffect {
    public StunEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        VFXUtils.createAndSpawnEffectInstance(entity, "stun", new Vec3d(0, entity.getHeight(), 0), 0.5f, true);
    }
}
