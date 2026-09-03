package com.boundless.mixin;

import com.boundless.client.CameraShake;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.util.CameraShakeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.damage.DamageSource;
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
public abstract class CameraMixin implements CameraShakeAccessor {
    @Shadow @Final MinecraftClient client;
    @Unique
    List<CameraShake> cameraShakes = new ArrayList<>();

    @Inject(method = "tiltViewWhenHurt", at = @At(value = "HEAD"), cancellable = true)
    private void boundless$cameraShake(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.player == null) return;

        DamageSource recentSource = client.player.getRecentDamageSource();

        if (!cameraShakes.isEmpty()) {
            cameraShakes.removeIf(CameraShake::isMarkFinished);
            cameraShakes.forEach((cameraShake -> {
                cameraShake.getCameraShakeLogic().accept(client, matrices);
            }));
        }

        if (recentSource != null && recentSource.isOf(DamageTypeRegistry.BLEED)) {
            ci.cancel();
            return;
        }
    }

    public void boundless$addCameraShake(CameraShake cameraShake) {
        cameraShakes.add(cameraShake);
    }
}
