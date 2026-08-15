package com.boundless.entity.divine_dogs;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.kuro.DivineDogAnimator;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;

public class DivineDogEntityRenderer<T extends WolfEntity> extends AzEntityRenderer<T> {
    private static final Identifier GEO = BoundlessAPI.identifier("geo/entity/divine_dog.geo.json");

    public DivineDogEntityRenderer(EntityRendererFactory.Context context, Identifier texture) {
        super(AzEntityRendererConfig.<T>builder(GEO, texture).setAnimatorProvider(DivineDogAnimator::new).build(), context);
    }

}
