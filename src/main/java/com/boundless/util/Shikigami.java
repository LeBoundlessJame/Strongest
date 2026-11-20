package com.boundless.util;

import net.minecraft.entity.LivingEntity;

public interface Shikigami {
    default void onSummon(LivingEntity user) {}
    default void onDesummon() {}
}
