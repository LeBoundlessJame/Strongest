package com.boundless.ability.reusable_abilities.flight;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.FlightAccess;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

public class FlightRendering {

    public static void hoverRendering(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer, CallbackInfo ci) {
        ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        if (clientPlayer == null || !clientPlayer.getUuid().equals(abstractClientPlayerEntity.getUuid())) return;
        float pitch = abstractClientPlayerEntity.getPitch(tickDelta);
        float rotationSpeed = 0.0075f;
        rotationSpeed *= abstractClientPlayerEntity.isSprinting() ? 3 : 0.35f;
        float clamp = abstractClientPlayerEntity.isSprinting() ? 1.0f : 0.2f;

        float rotationAmount = ((FlightAccess)renderer).boundless$getFlightRotation();

        if (clientPlayer.input.getMovementInput().y == 1) {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(rotationSpeed, -1.0f, clamp);
        } else if (clientPlayer.input.getMovementInput().y == -1) {
            ((FlightAccess)renderer).boundless$adjustFlightRotation(-rotationSpeed, -clamp, 1.0f);
        } else {
            if (rotationAmount > 0.3f) {
                rotationSpeed *= 3f;
            }
            ((FlightAccess)renderer).boundless$returnToDefaultRotation(rotationSpeed * 2f);
        }

        rotationAmount = ((FlightAccess)renderer).boundless$getFlightRotation();

        float degrees = rotationAmount * (-90.0F - pitch);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));

        if (abstractClientPlayerEntity.isSprinting()) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.sin((abstractClientPlayerEntity.age + tickDelta) * 0.1f) / 5f));
        } else {
            matrixStack.translate(0, (float) Math.sin((abstractClientPlayerEntity.age + tickDelta) * 0.1f) / 5f, 0);
        }
    }


    public static void renderFlight(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, PlayerEntityRenderer renderer) {
        float pitch = abstractClientPlayerEntity.getPitch(tickDelta);
        Map<String, KeybindHoldData> keyHeldMap = HeroUtils.getHeroStack(abstractClientPlayerEntity).getOrDefault(DataComponentRegistry.HELD_KEYBIND, new HashMap<String, KeybindHoldData>());
        KeybindHoldData keybindHoldData = keyHeldMap.get("key.forward");
        if (keybindHoldData == null) return;
        int elapsedTicks = Math.toIntExact(abstractClientPlayerEntity.getWorld().getTime() - keybindHoldData.startTimestamp());
        int duration = 40;
        float progress = Math.clamp((float) elapsedTicks / duration, 0f, 1f);
        abstractClientPlayerEntity.sendMessage(Text.of("Progress + " + String.valueOf(progress)), true);
        float rotationDegrees = MathHelper.lerp(progress, 0, 0.4f);
        rotationDegrees = rotationDegrees * (-90 - pitch);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotationDegrees));
    }
}
