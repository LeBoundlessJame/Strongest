package com.boundless.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class CameraVisibilityMixin {

    /* Todo: revisit
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

     */
}
