package com.boundless.entity.finger_bearer;

import com.boundless.BoundlessAPI;
import com.boundless.entity.malevolent_shrine.MalevolentShrineAnimator;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class FingerBearerEntityRenderer extends AzEntityRenderer<FingerBearerEntity> {
    private static final Identifier GEO = BoundlessAPI.id("geo/item/hero.geo.json");
    private static final Identifier TEX = BoundlessAPI.id("textures/entity/finger_bearer.png");

    public FingerBearerEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<FingerBearerEntity>builder(GEO, TEX).build(), context);
    }
}
