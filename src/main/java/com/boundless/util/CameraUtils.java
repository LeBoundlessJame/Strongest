package com.boundless.util;

import com.boundless.client.CameraShake;
import net.minecraft.client.MinecraftClient;

public class CameraUtils {
    public static void addCameraShake(MinecraftClient client, long duration, float intensity) {
        if (client.player == null || client.world == null || !client.player.getWorld().isClient) return;
        long worldTime = client.world.getTime();
        CameraShake cameraShake = new CameraShake();
        cameraShake.setStartTimestamp(worldTime);
        cameraShake.setEndTimestamp(worldTime + duration);
        cameraShake.setIntensity(intensity);
        ((CameraShakeAccessor)client.gameRenderer).boundless$addCameraShake(cameraShake);
    }

    public static void addCameraShake(MinecraftClient client, CameraShake cameraShake) {
        if (client.player == null || client.world == null || !client.player.getWorld().isClient) return;
        ((CameraShakeAccessor)client.gameRenderer).boundless$addCameraShake(cameraShake);
    }
}
