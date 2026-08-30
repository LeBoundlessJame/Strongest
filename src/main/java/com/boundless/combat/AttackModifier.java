package com.boundless.combat;

import net.minecraft.entity.player.PlayerEntity;

public interface AttackModifier {
    boolean shouldTrigger(PlayerEntity player);
    void apply(Hit hit);
    default void onTrigger(PlayerEntity player) {};
}
