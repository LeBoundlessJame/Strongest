package com.boundless.entity.gama.goals;

import com.boundless.entity.gama.GamaEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

public class ToadLeapGoal extends Goal {
    private final GamaEntity toad;
    private int cooldown;

    public ToadLeapGoal(GamaEntity toad) {
        this.toad = toad;
    }

    @Override
    public boolean canStart() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        return toad.isOnGround() && toad.getRandom().nextInt(20) == 0;
    }

    @Override
    public void start() {
        Vec3d direction = toad.getRotationVector();

        toad.setVelocity(direction.x * 0.8, 0.6, direction.z * 0.8);

        toad.velocityModified = true;
        cooldown = 15;
    }
}