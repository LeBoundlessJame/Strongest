package com.boundless.client;

import com.boundless.BoundlessAPI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector4f;

public class CursedFireRendering {

    public static void renderAllCursedFire(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation) {
        // Todo: this is messy. Could do with a cleanup at some stage, and make it scale based on reserves
        float scale = 2.5f + (float)Math.sin((entity.age + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * 0.05) * 0.25f;
        renderCursedFire(matrixStack, vertexConsumers, entity, rotation, new Vector4f(0.35f, 0.81f, 0.87f, 0.4f), scale, 0);

        scale = 2.0f + (float)Math.sin((entity.age + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * 0.05) * 0.25f;
        renderCursedFire(matrixStack, vertexConsumers, entity, rotation, new Vector4f(0.6f, 0.95f, 1.0f, 0.4f), scale, 0.32f);
    }

    // This took WAY too long lmao
    public static void renderCursedFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Quaternionf rotation, Vector4f color, float scale, float zOffset) {
        Identifier fire0 = BoundlessAPI.identifier("textures/block/cursed_fire_0.png");
        Identifier fire1 = BoundlessAPI.identifier("textures/block/cursed_fire_1.png");

        int totalFrames = 32;
        float frameProgress = entity.age % totalFrames;
        float vStep = 16f / 512f;
        float vMin = frameProgress * vStep;
        float vMax = vMin + vStep;

        matrices.push();
        float f = entity.getWidth() * scale;
        matrices.scale(f, f, f);
        float g = 0.5F;
        float j = 0.0F;
        float i = entity.getHeight() / f;
        matrices.multiply(rotation);
        matrices.translate(0.0F, 0.0F, 0.3F - (int) i * 0.02F + zOffset);
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

            drawCursedFireVertex(entry, vertexConsumer, -g, -j, k, uMax, vMax, color.x, color.y, color.z, color.w);
            drawCursedFireVertex(entry, vertexConsumer, g, -j, k, uMin, vMax, color.x, color.y, color.z, color.w);
            drawCursedFireVertex(entry, vertexConsumer, g, 1.4F - j, k, uMin, vMin, color.x, color.y, color.z, color.w);
            drawCursedFireVertex(entry, vertexConsumer, -g, 1.4F - j, k, uMax, vMin, color.x, color.y, color.z, color.w);

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
