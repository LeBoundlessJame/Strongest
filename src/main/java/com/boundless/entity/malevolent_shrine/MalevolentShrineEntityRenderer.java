package com.boundless.entity.malevolent_shrine;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MalevolentShrineEntityRenderer extends AzEntityRenderer<MalevolentShrineEntity> {
    private static final Identifier GEO = BoundlessAPI.identifier("geo/entity/shrine.geo.json");
    private static final Identifier TEX = BoundlessAPI.identifier("textures/entity/shrine.png");

    public MalevolentShrineEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<MalevolentShrineEntity>builder(GEO, TEX).setAnimatorProvider(MalevolentShrineAnimator::new).build(), context);
    }
}
