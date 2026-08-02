package com.boundless.entity.divine_dogs.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class KuroPounceAtTargetGoal extends Goal {
    private final MobEntity mob;
    private LivingEntity target;
    private final float velocity;

    public KuroPounceAtTargetGoal(MobEntity mob, float velocity) {
        this.mob = mob;
        this.velocity = velocity;
        this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            this.target = this.mob.getTarget();
            if (this.target == null) {
                return false;
            } else {
                double d = this.mob.squaredDistanceTo(this.target);
                if (d < 4.0 || d > 32.0) {
                    return false;
                } else {
                    return this.mob.isOnGround();
                }
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.isOnGround();
    }

    @Override
    public void start() {
        Vec3d vec3d = this.mob.getVelocity();
        Vec3d vec3d2 = new Vec3d(this.target.getX() - this.mob.getX(), 0.0, this.target.getZ() - this.mob.getZ());
        if (vec3d2.lengthSquared() > 1.0E-7) {
            vec3d2 = vec3d2.normalize().multiply(1.1f).add(vec3d.multiply(0.2));
        }

        this.mob.setVelocity(vec3d2.x, this.velocity * 0.5f, vec3d2.z);
    }
}

