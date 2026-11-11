package com.boundless.entity.nail;

import com.boundless.BoundlessAPI;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

// Todo: might be able to just make a generic "blank" renderer for any entity and reuse it, will check later
public class NailEntityRenderer extends EntityRenderer<PersistentProjectileEntity> {
    public NailEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity entity) {
        return BoundlessAPI.id("hero_action");
    }
}