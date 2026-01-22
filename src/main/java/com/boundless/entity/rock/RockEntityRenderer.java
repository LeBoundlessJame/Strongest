package com.boundless.entity.rock;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class RockEntityRenderer extends AzEntityRenderer<RockEntity> {
    private static final Identifier GEO = Identifier.of(BoundlessAPI.MOD_ID, "geo/entity/rock.geo.json");

    private static final Identifier TEX = Identifier.of(BoundlessAPI.MOD_ID, "textures/entity/deepslate.png");

    public RockEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<RockEntity>builder(GEO, TEX).setAnimatorProvider(RockEntityAnimator::new).build(), context);
    }
}