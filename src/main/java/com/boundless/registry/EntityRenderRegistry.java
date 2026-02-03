package com.boundless.registry;

import com.boundless.entity.hero_action.HeroActionRenderer;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntityRenderer;
import com.boundless.entity.open.OpenEntityRenderer;
import com.boundless.entity.rock.RockEntityRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class EntityRenderRegistry {
    public static void initialize() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.HERO_ACTION_ENTITY, HeroActionRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.ROCK, RockEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.OPEN_ENTITY, OpenEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.MALEVOLENT_SHRINE, MalevolentShrineEntityRenderer::new);
    }
}
