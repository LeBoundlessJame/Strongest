package com.boundless.mechanics;

import net.minecraft.entity.player.PlayerEntity;

public class HealthManager {
    public static void regenCursedEnergyTick(PlayerEntity player) {
        if (player.getWorld().isClient || player.age % 10 != 0) return;
        player.heal(getSorcererRegen(player));
    }

    public static float getSorcererRegen(PlayerEntity player) {
        return 1.66f;
    }
}
