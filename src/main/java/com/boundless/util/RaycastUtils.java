package com.boundless.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class RaycastUtils {
    public static EntityHitResult raycast(PlayerEntity playerEntity, float range) {
        if (!playerEntity.getWorld().isClient) {
            Vec3d vec3d = playerEntity.getCameraPosVec(1.0f);
            Vec3d vec3d2 = playerEntity.getRotationVec(1);
            Vec3d vec3d3 = vec3d.add(vec3d2.x * range, vec3d2.y * range, vec3d2.z * range);
            Box box = playerEntity.getBoundingBox().stretch(vec3d2.multiply(range)).expand(1, 1, 1);

            // todo: A message from Daomephsta I saw in the fabricmc discord helped with this!
            return ProjectileUtil.raycast(playerEntity, vec3d,
                    vec3d3, box, entity -> true, range * range);
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
