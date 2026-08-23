package com.boundless.entity.gama.abilities;

import com.boundless.action.Action;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.shadow_hero.ShadowHero;
import com.boundless.networking.payloads.UpdateDragPayload;
import com.boundless.util.ActionUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class GamaGrapple {
    public static void grappleLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        ItemStack heroStack = HeroUtils.getHeroStack(player);
        boolean isGrappling = heroStack.getOrDefault(ShadowHero.GRAPPLING, false);

        if (!isGrappling) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 128);
            if (blockHitResult == null) return;

            GrappleEntity grappleEntity = new GrappleEntity(player, player.getWorld());
            grappleEntity.setVelocity(0, 0, 0);
            grappleEntity.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grappleEntity.setNoGravity(true);
            player.getWorld().spawnEntity(grappleEntity);

            heroStack.set(ShadowHero.BOUND_GRAPPLE_HOOK_ID, grappleEntity.getId());
        } else {
            int boundHook = heroStack.getOrDefault(ShadowHero.BOUND_GRAPPLE_HOOK_ID, -1);
            if (boundHook == -1) return;

            GrappleEntity grappleEntity = (GrappleEntity) player.getWorld().getEntityById(boundHook);
            if (grappleEntity == null) return;
            grappleEntity.swingBoost(player);
            grappleEntity.discard();

            // Todo: maybe add a ticklogic that makes the entity persist until the player hits the ground, in which case
            // Todo: Send the packet for updating drag and discard the entity in the consumer
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            int maxLifetime = 10 * 20;

            BiConsumer<PlayerEntity, HeroActionEntity> customTickLogic = (user, heroAction) -> {
                if (user.getWorld().isClient) return;
                if (user.isOnGround()) {
                    ServerPlayNetworking.send((ServerPlayerEntity) user, new UpdateDragPayload(user.getUuid()));
                    heroAction.discard();
                }
            };

            tasks.put(maxLifetime, (user, heroAction) -> {
                if (user.getWorld().isClient) return;
                ServerPlayNetworking.send((ServerPlayerEntity) user, new UpdateDragPayload(user.getUuid()));
                heroAction.discard();
            });

            Action action = Action.builder().scheduledTasks(tasks).customTickLogic(customTickLogic).hitboxWidthZ(0).hitboxWidthX(0).hitboxHeight(0).build();
            ActionUtils.performAction(player, action);

            heroStack.set(ShadowHero.BOUND_GRAPPLE_HOOK_ID, -1);
        }

        heroStack.set(ShadowHero.GRAPPLING, !isGrappling);
    }
}
