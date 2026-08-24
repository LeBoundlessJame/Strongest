package com.boundless.entity.gama.abilities;

import com.boundless.action.Action;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.shadow_hero.ShadowHero;
import com.boundless.networking.payloads.UpdateDragPayload;
import com.boundless.util.ActionUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
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

        if (!(player.getControllingVehicle() instanceof GamaEntity gama) || gama.getOwner() != player) {
            return;
        }

        GrappleEntity grapple = gama.getGrapple();

        if (grapple == null) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 128);
            if (blockHitResult == null) return;

            grapple = new GrappleEntity(gama, player.getWorld());
            grapple.setVelocity(0, 0, 0);
            grapple.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grapple.setNoGravity(true);
            player.getWorld().spawnEntity(grapple);

            gama.setGrapple(grapple);
        } else {
            grapple.discard();
            gama.setGrapple(null);
        }
    }
}
