package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.DivineDogEntityRenderer;
import com.boundless.entity.gama.GamaEntityRenderer;
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
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.DIVINE_DOG_KURO, ctx -> new DivineDogEntityRenderer<>(ctx, BoundlessAPI.identifier("textures/entity/divine_dog_black.png")));
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.DIVINE_DOG_SHIRO, ctx -> new DivineDogEntityRenderer<>(ctx, BoundlessAPI.identifier("textures/entity/divine_dog_white.png")));
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.GAMA, ctx -> new GamaEntityRenderer<>(ctx));
    }
}
