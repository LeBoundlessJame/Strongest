package com.boundless.entity.gama;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class GamaEntityRenderer<T extends AnimalEntity> extends AzEntityRenderer<T> {
    private static final Identifier GEO = BoundlessAPI.id("geo/entity/gama.geo.json");
    private static final Identifier TEXTURE = BoundlessAPI.id("textures/entity/gama.png");

    public GamaEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<T>builder(GEO, TEXTURE).setAnimatorProvider(GamaAnimator::new).build(), context);
    }
}
