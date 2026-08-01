package com.boundless.entity.divine_dogs;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroAnimator;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.entity.malevolent_shrine.MalevolentShrineAnimator;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class DivineDogEntityRenderer extends AzEntityRenderer<DivineDogKuroEntity> {
    private static final Identifier GEO = BoundlessAPI.identifier("geo/entity/divine_dog.geo.json");
    private static final Identifier TEX = BoundlessAPI.identifier("textures/entity/divine_dog_black.png");

    public DivineDogEntityRenderer(EntityRendererFactory.Context context) {
        super(AzEntityRendererConfig.<DivineDogKuroEntity>builder(GEO, TEX).setAnimatorProvider(DivineDogKuroAnimator::new).build(), context);
    }
}
