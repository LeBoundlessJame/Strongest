package com.boundless.entity.divine_dogs.goals;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class KuroMeleeGoal extends MeleeAttackGoal {
    public KuroMeleeGoal(DivineDogKuroEntity kuro, double speed, boolean pauseWhenMobIdle) {
        super(kuro, speed, pauseWhenMobIdle);
    }

    @Override
    protected int getTickCount(int ticks) {
        return ticks / 2;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    protected void attack(LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(target);
            EffekUtils.playEffect(BoundlessAPI.identifier("claw_slashes"), target, target.getPos().add(0, 1, 0), new Vec3d(target.getHeight() * 0.3, target.getHeight() * 0.3, target.getHeight() * 0.3));
        }
    }
}
