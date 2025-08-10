package com.boundless.ability.reusable_abilities.flight;

import com.boundless.util.HeroUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class FlightRendering {
    public static void renderFlight(AbstractClientPlayerEntity player, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer) {
        if (player.getAbilities().flying) {
            float pitch = player.getPitch(tickDelta);
            float rotation = HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_ROTATION, 0f);
            float degrees = rotation * (-90.0F - pitch);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
        }

        if (player.isSprinting() && player.getAbilities().flying) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((player.age + tickDelta) * 0.1f) / 5f));
        } else if (player.getAbilities().flying) {
            matrixStack.translate(0, (float) Math.sin((player.age + tickDelta) * 0.1f) / 5f, 0);
        }
    }
}
