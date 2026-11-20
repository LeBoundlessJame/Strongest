package com.boundless.util;

import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class RaycastUtils {
    public static EntityHitResult raycast(LivingEntity user, float range) {
        if (!user.getWorld().isClient) {
            Vec3d start = user.getCameraPosVec(1.0f);
            Vec3d direction = user.getRotationVec(1);
            Vec3d end = start.add(direction.x * range, direction.y * range, direction.z * range);
            Box box = user.getBoundingBox().stretch(direction.multiply(range)).expand(1, 1, 1);

            // todo: A message from Daomephsta I saw in the fabricmc discord helped with this!
            return ProjectileUtil.raycast(user, start, end, box, entity -> {
                return !(entity instanceof HeroActionEntity);
            }, range * range);
        }
        return null;
    }

    public static Entity thickRaycast(LivingEntity user, float range, float raycastRadius) {
        if (!user.getWorld().isClient) {
            Vec3d start = user.getCameraPosVec(1.0f);
            Vec3d direction = user.getRotationVec(1.0f);
            Vec3d end = start.add(direction.x * range, direction.y * range, direction.z * range);
            Box box = user.getBoundingBox().stretch(direction.multiply(range)).expand(raycastRadius);

            Entity closestEntity = null;
            double closestDistance = range * range;

            for (Entity entity: user.getWorld().getOtherEntities(user, box)) {
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



    public static BlockHitResult blockRaycast(LivingEntity user, float range) {
        if (user.getWorld().isClient) return null;
        HitResult hitResult = user.raycast(range, 1.0f, false);
        if (!hitResult.getType().equals(HitResult.Type.BLOCK)) return null;
        return (BlockHitResult) hitResult;
    }
}
