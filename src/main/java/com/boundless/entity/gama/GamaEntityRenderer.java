package com.boundless.entity.gama;

import com.boundless.BoundlessAPI;
import com.boundless.entity.grapple.GrappleEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class GamaEntityRenderer<T extends GamaEntity> extends AzEntityRenderer<T> {
    private static final Identifier GEO = BoundlessAPI.id("geo/entity/gama.geo.json");
    private static final Identifier TEXTURE = BoundlessAPI.id("textures/entity/gama.png");

    public GamaEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<T>builder(GEO, TEXTURE).setAnimatorProvider(GamaAnimator::new).build(), context);
    }

    @Override
    public void render(
            @NotNull T gama,
            float entityYaw,
            float partialTick,
            @NotNull MatrixStack poseStack,
            @NotNull VertexConsumerProvider bufferSource,
            int packedLight
    ) {
        super.render(gama, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        if (gama.getPullTimer() <= 0) return;
        Entity target = gama.getPullTarget();

        if (target == null) return;
        GrappleEntityRenderer.renderRope(target, partialTick, poseStack, bufferSource, gama);
    }

    @Override
    public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
