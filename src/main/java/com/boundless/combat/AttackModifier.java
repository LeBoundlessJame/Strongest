package com.boundless.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface AttackModifier {
    boolean shouldTrigger(LivingEntity user);
    void apply(Hit hit);
    default void onTrigger(LivingEntity user) {};
    default void postTrigger(LivingEntity user) {};
}
