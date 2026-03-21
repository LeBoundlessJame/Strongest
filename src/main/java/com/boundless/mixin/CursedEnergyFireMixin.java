package com.boundless.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class CursedEnergyFireMixin {

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private void boundless$renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation, CallbackInfo ci) {
        boundless$renderTransparentFire(matrices, vertexConsumers, entity, rotation);
        ci.cancel();
    }

    @Unique
    private void boundless$renderTransparentFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation) {
        Sprite sprite = ModelLoader.FIRE_0.getSprite();
        Sprite sprite2 = ModelLoader.FIRE_1.getSprite();
        matrices.push();
        float f = entity.getWidth() * 2F;
        matrices.scale(f, f, f);
        float g = 0.5F;
        float h = 0.0F;
        float i = entity.getHeight() / f;
        float j = 0.0F;
        matrices.multiply(rotation);
        matrices.translate(0.0F, 0.0F, 0.3F - (int)i * 0.02F);
        float k = 0.0F;
        int l = 0;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));

        for (MatrixStack.Entry entry = matrices.peek(); i > 0.0F; l++) {
            Sprite sprite3 = l % 2 == 0 ? sprite : sprite2;
            float m = sprite3.getMinU();
            float n = sprite3.getMinV();
            float o = sprite3.getMaxU();
            float p = sprite3.getMaxV();
            if (l / 2 % 2 == 0) {
                float q = o;
                o = m;
                m = q;
            }

            float red = 0.2f;
            float green = 0.6f;
            float blue = 1.0f;
            float alpha = 0.4f;

            boundless$drawFireVertex(entry, vertexConsumer, -g - 0.0F, 0.0F - j, k, o, p, red, green, blue, alpha);
            boundless$drawFireVertex(entry, vertexConsumer, g - 0.0F, 0.0F - j, k, m, p, red, green, blue, alpha);
            boundless$drawFireVertex(entry, vertexConsumer, g - 0.0F, 1.4F - j, k, m, n, red, green, blue, alpha);
            boundless$drawFireVertex(entry, vertexConsumer, -g - 0.0F, 1.4F - j, k, o, n, red, green, blue, alpha);
            i -= 0.45F;
            j -= 0.45F;
            g *= 0.9F;
            k -= 0.03F;
        }

        matrices.pop();
    }

    @Unique
    private static void boundless$drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha) {
        vertices.vertex(entry, x, y, z)
                .color(red, green, blue, alpha)
                .texture(u, v)
                .overlay(0, 10)
                .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(entry, 0.0F, 1.0F, 0.0F);
    }

}
