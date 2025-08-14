package com.boundless.client;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.function.BiConsumer;

@Setter
public class CameraShake {
    @Getter
    private BiConsumer<MinecraftClient, MatrixStack> cameraShakeLogic = this::shake;
    private int duration = 20;
    private float intensity = 1.0f;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    @Getter
    private boolean markFinished = false;

    private int calculateElapsedTicks(long currentTimestamp) {
        return Math.toIntExact(currentTimestamp - startTimestamp);
    }

    public void shake(MinecraftClient client, MatrixStack matrices) {
        if (!shouldShake(client) || client.player == null) return;

        long worldTime = client.player.getWorld().getTime();
        float tickDelta = client.getRenderTickCounter().getTickDelta(true);
        int elapsedTicks = calculateElapsedTicks(worldTime);

        float rotationDegrees = MathHelper.lerp((float) elapsedTicks / duration, (float) Math.sin((elapsedTicks * intensity + tickDelta)), 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationDegrees));
    }

    public boolean shouldShake(MinecraftClient client) {
        if (this.startTimestamp == 0 || this.endTimestamp == 0 || client.player == null) return false;

        long worldTime = client.player.getWorld().getTime();
        if (worldTime > this.endTimestamp) {
            this.markFinished = true;
            return false;
        }
        return true;
    }
}
