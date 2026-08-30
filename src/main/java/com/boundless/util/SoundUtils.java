package com.boundless.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class SoundUtils {

    public static void playSound(LivingEntity entity, SoundEvent sound, float pitch) {
        if (!entity.getWorld().isClient) {
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundCategory.PLAYERS, 1.0f, pitch);
        }
    }

    public static void playSound(LivingEntity entity, SoundEvent sound) {
        float pitch = entity.getRandom().nextBetween(6, 15) * 0.1f;
        playSound(entity, sound, pitch);
    }

    public static void playSound(LivingEntity entity, SoundEvent sound, int lowerRange, int upperRange) {
        float pitch = entity.getRandom().nextBetween(lowerRange, upperRange) * 0.1f;
        playSound(entity, sound, pitch);
    }
}
