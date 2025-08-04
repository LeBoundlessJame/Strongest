package com.boundless.ability.reusable_abilities.flight;

import com.boundless.util.FlightAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class FlightRendering {

    public static void hoverRendering(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer == null || !clientPlayer.getUuid().equals(abstractClientPlayerEntity.getUuid())) return;
        float pitch = abstractClientPlayerEntity.getPitch(tickDelta);
        float rotationSpeed = 0.0075f;
        rotationSpeed *= abstractClientPlayerEntity.isSprinting() ? 3 : 0.35f;

        if (clientPlayer.input.getMovementInput().y == 1) {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(rotationSpeed, -1.0f, 0.2f);
        } else if (clientPlayer.input.getMovementInput().y == -1) {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(-rotationSpeed, -0.2f, 1.0f);
        } else {
            ((FlightAccess)renderer).boundless$returnToDefaultRotation(rotationSpeed * 2f);
        }

        float rotationAmount = ((FlightAccess)renderer).boundless$getFlightRotation();
        abstractClientPlayerEntity.sendMessage(Text.of("Rotation amount : " + rotationAmount), true);
        float degrees = rotationAmount * (-90.0F - pitch);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((abstractClientPlayerEntity.age + tickDelta) * 0.1f) / 5f));

        /*
        if (abstractClientPlayerEntity.isSprinting()) {
            Vec3d rotation = abstractClientPlayerEntity.getRotationVec(tickDelta);
            double d = lerpedVelocity.horizontalLengthSquared();
            double e = rotation.horizontalLengthSquared();

            if (d > 0.0 && e > 0.0) {
                double n = (lerpedVelocity.x * rotation.x + lerpedVelocity.z * rotation.z) / Math.sqrt(d * e);
                double o = lerpedVelocity.x * rotation.z - lerpedVelocity.z * rotation.x;

                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(o) * Math.acos(n))));


            }

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((abstractClientPlayerEntity.age + tickDelta) * 0.1f) / 5f));
        }
        */
    }
}
