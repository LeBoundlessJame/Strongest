package com.boundless.util;

import net.minecraft.entity.player.PlayerEntity;

public interface Shikigami {
    default void onSummon(PlayerEntity player) {}
    default void onDesummon() {}
}
