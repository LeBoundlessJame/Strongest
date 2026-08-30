package com.boundless.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void renderRope(Vec3d start, Vec3d end, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        float thickness = 0.075f;

        float xDif = (float)(end.x - start.x);
        float yDif = (float)(end.y - start.y);
        float zDif = (float)(end.z - start.z);

        float offsetMod = MathHelper.inverseSqrt(xDif * xDif + zDif * zDif) * thickness / 2.0F;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderLayer.getLeash());
        Matrix4f posMatrix = new Matrix4f(poseStack.peek().getPositionMatrix());

        int segment;
        for(segment = 0; segment <= 24; ++segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, packedLight, thickness, thickness, xOffset, zOffset, segment, false);
        }

        for(segment = 24; segment >= 0; --segment) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, packedLight, thickness, 0.0F, xOffset, zOffset, segment, true);
        }
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif, float zDif, int packedLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = (float)segment / 24.0F;
        float knotColourMod = 1.0f;
        float red = 0.64f * knotColourMod;
        float green = 0.39f * knotColourMod;
        float blue = 0.60f * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0F ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0F - piecePosPercent) * (1.0F - piecePosPercent);
        float z = zDif * piecePosPercent;
        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1.0F).light(packedLight);
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1.0F).light(packedLight);
    }
}
