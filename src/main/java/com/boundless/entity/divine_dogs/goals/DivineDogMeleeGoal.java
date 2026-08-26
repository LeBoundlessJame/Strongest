package com.boundless.entity.divine_dogs.goals;

import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.registry.StatusEffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

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

            if (this.mob.tryAttack(target) && this.mob instanceof DivineDogKuroEntity kuro) {
                tryApplyBleed(kuro, target);
            }
        }
    }

    private void tryApplyBleed(DivineDogKuroEntity kuro, LivingEntity target) {
        if (kuro.getRandom().nextInt(100) >= 15) return;
        StatusEffectInstance enemyBleed = target.getStatusEffect(StatusEffectRegistry.BLEED);
        int amplifier = enemyBleed == null ? 0 : MathHelper.clamp(enemyBleed.getAmplifier() + 1, 0, 2);

        target.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.BLEED, 200, amplifier, false, false, true));
    }
}
