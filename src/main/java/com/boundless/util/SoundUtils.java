package com.boundless.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class SoundUtils {

    public static void playSound(PlayerEntity player, SoundEvent sound, float pitch) {
        if (!player.getWorld().isClient) {
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundCategory.PLAYERS, 1.0f, pitch);
        }
    }

    public static void playSound(PlayerEntity player, SoundEvent sound) {
        float pitch = player.getRandom().nextBetween(6, 15) * 0.1f;
        playSound(player, sound, pitch);
    }
}
