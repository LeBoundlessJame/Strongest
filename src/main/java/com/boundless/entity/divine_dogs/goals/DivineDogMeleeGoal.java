package com.boundless.entity.divine_dogs.goals;

import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Hand;

public class DivineDogMeleeGoal<T extends WolfEntity> extends MeleeAttackGoal {
    public DivineDogMeleeGoal(T divineDog, double speed, boolean pauseWhenMobIdle) {
        super(divineDog, speed, pauseWhenMobIdle);
    }

    @Override
    protected int getTickCount(int ticks) {
        return ticks;
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
        }
    }
}
