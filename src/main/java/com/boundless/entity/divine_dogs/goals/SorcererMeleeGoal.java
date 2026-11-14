package com.boundless.entity.divine_dogs.goals;

import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.entity.finger_bearer.FingerBearerEntity;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class SorcererMeleeGoal <T extends PathAwareEntity> extends MeleeAttackGoal {
    public SorcererMeleeGoal(T entity, double speed, boolean pauseWhenMobIdle) {
        super(entity, speed, pauseWhenMobIdle);
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

            if (this.mob instanceof FingerBearerEntity fingerBearer) {
                fingerBearer.dispatcher.chargeUp();
            }

            TickScheduler.schedule(this.mob.getWorld(), 17, () -> {
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);
            });
        }
    }
}
