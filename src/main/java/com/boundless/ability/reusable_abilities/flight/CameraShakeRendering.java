package com.boundless.ability.reusable_abilities.flight;

import com.boundless.util.HeroUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CameraShakeRendering {
    public static void cameraShake(MatrixStack matrices, float tickDelta, ClientPlayerEntity player, CallbackInfo ci) {
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
}
