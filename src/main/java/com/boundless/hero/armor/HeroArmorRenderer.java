package com.boundless.hero.armor;

import mod.azure.azurelib.common.render.AzRendererPipeline;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import mod.azure.azurelib.common.render.armor.AzArmorRendererPipelineContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public class HeroArmorRenderer extends AzArmorRenderer {

    public HeroArmorRenderer(BiFunction<Entity, ItemStack, Identifier> model, BiFunction<Entity, ItemStack, Identifier> texture) {
        super(AzArmorRendererConfig.builder(model, texture).setPipelineContext(pipelineContext()).build());
    }

    public HeroArmorRenderer(Identifier model, Identifier texture) {
        this((entity, stack) -> model, (entity, stack) -> texture);
    }

    private static Function<AzRendererPipeline<UUID, ItemStack>, AzRendererPipelineContext<UUID, ItemStack>> pipelineContext() {
        return pipeline -> new AzArmorRendererPipelineContext(pipeline) {
            @Override
            public RenderLayer getDefaultRenderType(ItemStack stack, Identifier texture, @Nullable VertexConsumerProvider bufferSource, float partialTick, RenderLayer defaultRenderType, float alpha) {
                return RenderLayer.getEntityCutoutNoCull(texture);
            }

            @Override
            protected int getPackedOverlay(ItemStack animatable, float u, float partialTick) {
                if (!(currentEntity instanceof LivingEntity livingEntity)) return OverlayTexture.DEFAULT_UV;

                return OverlayTexture.packUv(OverlayTexture.getU(0), OverlayTexture.getV(livingEntity.hurtTime > 0 || livingEntity.deathTime > 0));
            }
        };
    }
}
