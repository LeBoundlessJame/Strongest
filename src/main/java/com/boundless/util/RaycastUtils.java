package com.boundless.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class RaycastUtils {
    public static EntityHitResult raycast(PlayerEntity playerEntity, float range) {
        if (!playerEntity.getWorld().isClient) {
            Vec3d start = playerEntity.getCameraPosVec(1.0f);
            Vec3d direction = playerEntity.getRotationVec(1);
            Vec3d end = start.add(direction.x * range, direction.y * range, direction.z * range);
            Box box = playerEntity.getBoundingBox().stretch(direction.multiply(range)).expand(1, 1, 1);

            // todo: A message from Daomephsta I saw in the fabricmc discord helped with this!
            return ProjectileUtil.raycast(playerEntity, start,
                    end, box, entity -> true, range * range);
        }
        return null;
    }

    public static Entity thickRaycast(PlayerEntity playerEntity, float range, float raycastRadius) {
        if (!playerEntity.getWorld().isClient) {
            Vec3d start = playerEntity.getCameraPosVec(1.0f);
            Vec3d direction = playerEntity.getRotationVec(1.0f);
            Vec3d end = start.add(direction.x * range, direction.y * range, direction.z * range);
            Box box = playerEntity.getBoundingBox().stretch(direction.multiply(range)).expand(raycastRadius);

            Entity closestEntity = null;
            double closestDistance = range * range;

            for (Entity entity: playerEntity.getWorld().getOtherEntities(playerEntity, box)) {
                Box entityHitbox = entity.getBoundingBox().expand(raycastRadius);

                Optional<Vec3d> raycastResult = entityHitbox.raycast(start, end);
                if (raycastResult.isPresent() && start.squaredDistanceTo(raycastResult.get()) < closestDistance) {
                    closestDistance = raycastResult.get().squaredDistanceTo(start);
                    closestEntity = entity;
                }
            }

            return closestEntity;
        }
        return null;
    }



    public static BlockHitResult blockRaycast(PlayerEntity player, float range) {
        if (player.getWorld().isClient) return null;
        HitResult hitResult = player.raycast(range, 1.0f, false);
        if (!hitResult.getType().equals(HitResult.Type.BLOCK)) return null;
        return (BlockHitResult) hitResult;
    }
}
