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
    private int duration = 40;
    private float intensity = 1.2f;
    private long startTimestamp = 0;
    private long endTimestamp = 0;
    @Getter
    private boolean markFinished = false;

    private int calculateElapsedTicks(long currentTimestamp) {
        return Math.toIntExact(currentTimestamp - startTimestamp);
    }

    public void shake(MinecraftClient client, MatrixStack matrices) {
        if (this.startTimestamp == 0 || this.endTimestamp == 0 || client.player == null) return;

        long worldTime = client.player.getWorld().getTime();
        if (worldTime > this.endTimestamp) {
            this.markFinished = true;
            return;
        }

        float tickDelta = client.getRenderTickCounter().getTickDelta(true);
        int elapsedTicks = calculateElapsedTicks(worldTime);

        float rotationDegrees = MathHelper.lerp((float) elapsedTicks / duration, (float) Math.sin((elapsedTicks * intensity + tickDelta)), 0);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationDegrees));
    }

    /*
    public static void shakeLogic() {
        int flightTicksServer = HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_TICKS, 0);
        long flightBegin = HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_BEGIN_TIMESTAMP, 0).longValue();
        int flightTicks = Math.toIntExact(player.clientWorld.getTime() - flightBegin);

        int shakeDuration = 40;
        if (flightTicksServer <= 0) return;

        if (flightTicksServer < shakeDuration) {
            float intensity = 1.2f;
            float rotationDegrees = MathHelper.lerp((float) flightTicks / (float) shakeDuration, (float) Math.sin((flightTicks * intensity + tickDelta)), 0);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotationDegrees));
        }
    }

     */
}
