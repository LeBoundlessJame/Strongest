package com.boundless.mixin;

import com.boundless.client.CameraShake;
import com.boundless.util.CameraShakeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GameRenderer.class)
public abstract class CameraShakeMixin implements CameraShakeAccessor {
    @Shadow @Final MinecraftClient client;
    @Unique List<CameraShake> cameraShakes = new ArrayList<>();

    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"))
    private void boundless$cameraShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.player == null || cameraShakes.isEmpty()) return;
        cameraShakes.removeIf(CameraShake::isMarkFinished);
        cameraShakes.forEach((cameraShake -> {
            cameraShake.getCameraShakeLogic().accept(client, matrices);
        }));
    }

    @Override
    public void boundless$addCameraShake(CameraShake cameraShake) {
        cameraShakes.add(cameraShake);
    }
}
