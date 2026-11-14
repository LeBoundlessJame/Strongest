package com.boundless.entity.finger_bearer;

import com.boundless.entity.divine_dogs.goals.DivineDogMeleeGoal;
import com.boundless.entity.divine_dogs.goals.SorcererMeleeGoal;
import com.boundless.entity.divine_dogs.kuro.DivineDogDispatcher;
import lombok.Getter;
import lombok.Setter;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class FingerBearerEntity extends HostileEntity {
    public final MoveAnalysis moveAnalysis;
    public final FingerBearerDispatcher dispatcher;

    public FingerBearerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new FingerBearerDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SorcererMeleeGoal<>(this, 1.0, true));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
    }

    public boolean shouldAngerAt(LivingEntity entity) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.moveAnalysis.update();
        this.animationTick();
    }

    private void animationTick() {
        if (!this.getWorld().isClient) return;

        this.dispatcher.walk();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 435.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.2)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 64);
    }
}
