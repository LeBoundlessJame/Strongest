package com.boundless.client;

import com.boundless.BoundlessAPI;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

public class CursedFireRendering {

    // This took WAY too long lmao
    public static void renderCursedFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation) {
        Identifier fire0 = BoundlessAPI.identifier("textures/block/cursed_fire_0.png");
        Identifier fire1 = BoundlessAPI.identifier("textures/block/cursed_fire_1.png");

        int totalFrames = 32;
        float frameProgress = entity.age % totalFrames;
        float vStep = 16f / 512f;
        float vMin = frameProgress * vStep;
        float vMax = vMin + vStep;

        matrices.push();
        float f = entity.getWidth() * 2f;
        matrices.scale(f, f, f);
        float g = 0.5F;
        float j = 0.0F;
        float i = entity.getHeight() / f;
        matrices.multiply(rotation);
        matrices.translate(0.0F, 0.0F, 0.3F - (int) i * 0.02F);
        float k = 0.0F;
        int l = 0;

        for (MatrixStack.Entry entry = matrices.peek(); i > 0.0F; l++) {
            Identifier texture = l % 2 == 0 ? fire0 : fire1;
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(texture));

            float uMin = 0.0F;
            float uMax = 1.0F;

            if (l / 2 % 2 == 0) {
                float temp = uMax;
                uMax = uMin;
                uMin = temp;
            }

            float red = 0.2f;
            float green = 0.6f;
            float blue = 1.0f;
            float alpha = 0.4f;

            drawCursedFireVertex(entry, vertexConsumer, -g, -j, k, uMax, vMax, red, green, blue, alpha);
            drawCursedFireVertex(entry, vertexConsumer, g, -j, k, uMin, vMax, red, green, blue, alpha);
            drawCursedFireVertex(entry, vertexConsumer, g, 1.4F - j, k, uMin, vMin, red, green, blue, alpha);
            drawCursedFireVertex(entry, vertexConsumer, -g, 1.4F - j, k, uMax, vMin, red, green, blue, alpha);

            i -= 0.45F;
            j -= 0.45F;
            g *= 0.9F;
            k -= 0.03F;
        }

        matrices.pop();
    }

    private static void drawCursedFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha) {
        vertices.vertex(entry, x, y, z)
                .color(red, green, blue, alpha)
                .texture(u, v)
                .overlay(0, 10)
                .light(LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE)
                .normal(entry, 0.0F, 1.0F, 0.0F);
    }
}
