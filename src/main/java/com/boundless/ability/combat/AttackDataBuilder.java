package com.boundless.ability.combat;

import com.boundless.registry.SoundRegistry;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

@Getter @Setter @Builder
public class AttackDataBuilder {

    @Builder.Default
    PlayerEntity attacker = null;

    @Builder.Default
    float damage = 0;
    @Builder.Default
    float knockbackStrength = 1f;
    @Builder.Default
    DamageSource damageSource = null;

    @Builder.Default
    SoundEvent impactSound = SoundRegistry.IMPACT_HEAVY_1;
    @Builder.Default
    SoundEvent missSound = SoundRegistry.MISS_HIT;

    @Builder.Default
    BiConsumer<AttackDataBuilder, LivingEntity> customHitLogic = null;

    @Builder.Default
    BiConsumer<AttackDataBuilder, PlayerEntity> replacedAttackLogic = null;

    @Builder.Default
    BiConsumer<AttackDataBuilder, PlayerEntity> postHitLogic = null;

    @Builder.Default
    Identifier impactVisual = null;

    @Builder.Default
    int slownessDuration = 20;
    @Builder.Default
    int slownessAmplifier = 0;

    @Builder.Default
    Identifier animation = null;
    @Builder.Default
    float animationSpeed = 1.0f;
    @Builder.Default
    int impactTick = 5;
}
