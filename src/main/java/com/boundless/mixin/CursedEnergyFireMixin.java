package com.boundless.mixin;

import com.boundless.client.CursedFireRendering;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class CursedEnergyFireMixin {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private void boundless$renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation, CallbackInfo ci) {
        CursedFireRendering.renderAllCursedFire(matrices, vertexConsumers, entity, rotation);
        ci.cancel();
    }
}
