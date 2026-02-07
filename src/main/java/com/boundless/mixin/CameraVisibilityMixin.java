package com.boundless.mixin;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class CameraVisibilityMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;"))
    private Entity boundless$getFocusedEntity(Camera camera) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player != null && HeroUtils.isHero(player)) {
            if (HeroUtils.getHeroStack(player).get(DataComponentRegistry.BOUND_CAMERA_ID) != null) return player;
        }

        return camera.getFocusedEntity();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/render/Frustum;DDD)Z"))
    private boolean boundless$shouldRender(EntityRenderDispatcher dispatcher, Entity entity, Frustum frustum, double x, double y, double z) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && entity == client.player && HeroUtils.isHero(client.player)) {
            if (HeroUtils.getHeroStack(client.player).get(DataComponentRegistry.BOUND_CAMERA_ID) != null) return true;
        }

        return dispatcher.shouldRender(entity, frustum, x, y, z);
    }
}
