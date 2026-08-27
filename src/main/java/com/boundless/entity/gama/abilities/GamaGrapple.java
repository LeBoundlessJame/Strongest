package com.boundless.entity.gama.abilities;

import com.boundless.entity.gama.GamaEntity;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

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
