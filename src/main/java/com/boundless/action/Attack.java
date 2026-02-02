package com.boundless.action;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

@Builder @Getter
public class Attack {
    PlayerEntity player;
    @Builder.Default
    float damage = 1f;
    @Builder.Default
    SoundEvent impactSound = SoundRegistry.MISS_HIT;
    @Builder.Default
    Identifier impactVFX = BoundlessAPI.identifier("melee_impact");
    @Builder.Default
    Identifier animation = BoundlessAPI.identifier("hook");
    @Builder.Default
    float animationSpeed = 1.0f;
    @Builder.Default
    int animationPriority = 2000;
    @Builder.Default
    int impactTick = 1;
    @Builder.Default
    int attackDuration = 10;
    @Builder.Default
    BiConsumer<PlayerEntity, Entity> perEntityLogic = null;
}
