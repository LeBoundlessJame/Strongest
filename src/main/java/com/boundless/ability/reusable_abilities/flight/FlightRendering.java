package com.boundless.ability.reusable_abilities.flight;

import com.boundless.util.FlightAccess;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class FlightRendering {

    public static void clientInputLogic(MinecraftClient client) {
         /* Todo: fix flightDirection
        String flightDirection = HeroUtils.getHeroStack(abstractClientPlayerEntity).getOrDefault(SuperHero.FLIGHT_DIRECTION, "forward");
         */

    }

    public static void hoverRendering(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer, CallbackInfo ci) {
        float pitch = abstractClientPlayerEntity.getPitch(tickDelta);
        Vec3d lerpedVelocity = abstractClientPlayerEntity.lerpVelocity(tickDelta);
        double velocityLength = lerpedVelocity.length();
        float rotationAmount = 0f;

        if (velocityLength > 0) {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(0.05f, -0.2f, 0.2f);
        } else {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(-0.05f, -0.2f, 0.2f);
        }

        rotationAmount = ((FlightAccess)renderer).boundless$getFlightRotation();
        abstractClientPlayerEntity.sendMessage(Text.of("Rotation amount : " + rotationAmount), true);


        //rotationAmount = (float) MathHelper.clamp(velocityLength, -0.2f, 0.2f);
        //abstractClientPlayerEntity.sendMessage(Text.of("Velocity length: " + velocityLength), true);
        float degrees = rotationAmount * (-90.0F - pitch);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));

        /* Flight rendering
        ItemStack heroStack = HeroUtils.getHeroStack(abstractClientPlayerEntity);
        int flightTicksServer = heroStack.getOrDefault(SuperHero.FLIGHT_TICKS, 0);
        long flightBeginTimeStamp = heroStack.getOrDefault(SuperHero.FLIGHT_BEGIN_TIMESTAMP, 0L);
        long flightTicks = abstractClientPlayerEntity.clientWorld.getTime() - flightBeginTimeStamp;

        if (flightTicksServer > 0) {
            float l = (float) flightTicks + tickDelta;
            rotationAmount = (float) MathHelper.clamp(l / 10, -1f, 1f);

            float degrees = rotationAmount * (-90.0F - pitch);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
        }


         */

        if (abstractClientPlayerEntity.isSprinting()) {
            Vec3d rotation = abstractClientPlayerEntity.getRotationVec(tickDelta);
            double d = lerpedVelocity.horizontalLengthSquared();
            double e = rotation.horizontalLengthSquared();

            if (d > 0.0 && e > 0.0) {
                double n = (lerpedVelocity.x * rotation.x + lerpedVelocity.z * rotation.z) / Math.sqrt(d * e);
                double o = lerpedVelocity.x * rotation.z - lerpedVelocity.z * rotation.x;
                /*
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(o) * Math.acos(n))));

                 */
            }

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((abstractClientPlayerEntity.age + tickDelta) * 0.1f) / 5f));
        }
    }

    public static void flightRendering(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, CallbackInfo ci) {
        ItemStack heroStack = HeroUtils.getHeroStack(abstractClientPlayerEntity);
//abstractClientPlayerEntity.sendMessage(Text.of(String.valueOf(abstractClientPlayerEntity.getVelocity())), true);

        float pitch = abstractClientPlayerEntity.getPitch(tickDelta);

        Vec3d rotation = abstractClientPlayerEntity.getRotationVec(tickDelta);
        Vec3d lerpedVelocity = abstractClientPlayerEntity.lerpVelocity(tickDelta);
        double d = lerpedVelocity.horizontalLengthSquared();
        double e = rotation.horizontalLengthSquared();

        if (d > 0.0 && e > 0.0) {
            double n = (lerpedVelocity.x * rotation.x + lerpedVelocity.z * rotation.z) / Math.sqrt(d * e);
            double o = lerpedVelocity.x * rotation.z - lerpedVelocity.z * rotation.x;
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(o) * Math.acos(n))));
        }
    }
}
