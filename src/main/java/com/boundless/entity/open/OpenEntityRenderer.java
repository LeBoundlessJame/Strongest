package com.boundless.entity.open;

import com.boundless.BoundlessAPI;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

public class OpenEntityRenderer extends EntityRenderer<PersistentProjectileEntity> {
    public OpenEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity entity) {
        return BoundlessAPI.id("hero_action");
    }
}