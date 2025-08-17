package com.boundless.ability.reusable_abilities.flight;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.HashMap;

public class FlightRendering {
    public static HashMap<AbstractClientPlayerEntity, Float> flightRotations = new HashMap<>();

    public static void renderFlight(AbstractClientPlayerEntity player, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer) {
        KeybindHoldData forwardData = KeybindingUtils.getHoldData(player, "key.forward");
        KeybindHoldData backData = KeybindingUtils.getHoldData(player, "key.back");
        boolean isFlying = HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLYING, false);
        if (!isFlying) return;

        // Todo: make this follow a tick-based system for speed, etc.
        float rotation = flightRotations.getOrDefault(player, 0f);
        int duration = player.isSprinting() && rotation > 0 ? 15 : 45;
        float clamp = player.isSprinting() ? 1.0f : 0.2f;
        float rotationSpeed = clamp / duration;

        if (forwardData.held()) {
            rotation = MathHelper.clamp(rotation + rotationSpeed, -clamp, clamp);
        } else if (rotation > 0) {
            rotation = MathHelper.clamp(rotation - rotationSpeed, -clamp, clamp);
        } else if (backData.held()) {
            rotation = MathHelper.clamp(rotation - rotationSpeed, -clamp, 0f);
        } else if (rotation < 0) {
            rotation = MathHelper.clamp(rotation + rotationSpeed, -clamp, 0f);
        }

        float degrees = rotation * (-90.0F - player.getPitch(tickDelta));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
        flightRotations.put(player, rotation);


        float worldTime = (float) player.getWorld().getTime();
        if (player.isSprinting()) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((worldTime + tickDelta) * 0.1f) / 5f));
        } else {
            matrixStack.translate(0, (float) Math.sin((worldTime + tickDelta) * 0.1f) / 5f, 0);
        }
    }
}
