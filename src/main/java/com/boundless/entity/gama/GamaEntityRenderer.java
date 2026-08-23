package com.boundless.entity.gama;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.kuro.DivineDogAnimator;
import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.layer.AzBlockAndItemLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.UUID;

public class GamaEntityRenderer<T extends AnimalEntity> extends AzEntityRenderer<T> {
    private static final Identifier GEO = BoundlessAPI.identifier("geo/entity/gama.geo.json");
    private static final Identifier TEXTURE = BoundlessAPI.identifier("textures/entity/gama.png");

    public GamaEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<T>builder(GEO, TEXTURE).setAnimatorProvider(GamaAnimator::new).build(), context);
    }
}
