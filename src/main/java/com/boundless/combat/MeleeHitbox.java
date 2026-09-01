package com.boundless.combat;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class MeleeHitbox {
    private final double radius;
    private final double arcDegrees;

    public MeleeHitbox(double radius, double arcDegrees) {
        this.radius = radius;
        this.arcDegrees = arcDegrees;
    }

    public List<LivingEntity> getTargetsInArc(PlayerEntity user) {
        Vec3d origin = user.getEyePos();
        Vec3d facing = user.getRotationVector();

        double cosHalfArc = Math.cos(Math.toRadians(arcDegrees / 2.0));
        double radiusSquared = radius * radius;

        Box box = user.getBoundingBox().expand(radius);

        return user.getWorld().getEntitiesByClass(LivingEntity.class, box, target -> {
            if (target == user) return false;

            Vec3d direction = target.getEyePos().subtract(origin);
            if (direction.lengthSquared() > radiusSquared) return false;
            return facing.dotProduct(direction.normalize()) >= cosHalfArc;
        });
    }
}
