package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.CameraShakeRendering;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class FlightCameraMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract void loadProjectionMatrix(Matrix4f projectionMatrix);

    @Shadow public abstract Matrix4f getBasicProjectionMatrix(double fov);

    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"))
    private void boundless$cameraShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.player == null) return;
        CameraShakeRendering.cameraShake(matrices, tickDelta, client.player, ci);
    }
}
