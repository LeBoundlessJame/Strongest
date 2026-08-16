package com.boundless.entity.divine_dogs.kuro;

import com.boundless.entity.divine_dogs.goals.DivineDogMeleeGoal;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.Shikigami;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class DivineDogKuroEntity extends WolfEntity implements Shikigami {
    public final DivineDogDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;

    public DivineDogKuroEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new DivineDogDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public DivineDogKuroEntity(World world, PlayerEntity owner) {
        super(EntityRegistry.DIVINE_DOG_KURO, world);
        this.dispatcher = new DivineDogDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.setOwner(owner);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new DivineDogMeleeGoal(this, 1.0, true));
        this.goalSelector.add(9, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0f));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
    }

    @Override
    public boolean shouldAngerAt(LivingEntity entity) {
        return super.shouldAngerAt(entity);
    }

    @Override
    public boolean shouldTryTeleportToOwner() {
        LivingEntity livingEntity = this.getOwner();
        return livingEntity != null && this.squaredDistanceTo(this.getOwner()) >= (2048);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        this.animationTick();
    }

    public void animationTick() {
        if (!this.getWorld().isClient) return;

        if (this.isAttacking()) {
            this.dispatcher.slash();
            return;
        }

        if (!this.isInSittingPose()) {
            boolean isMovingOnGround = this.moveAnalysis.isMovingHorizontally() && this.isOnGround();

            if (isMovingOnGround) {
                if (this.hasAngerTime()) {
                    this.dispatcher.run();
                } else {
                    this.dispatcher.walk();
                }
            } else {
                this.dispatcher.idle();
            }
        } else {
            this.dispatcher.layIdle();
        }
    }

    // Taming overrides health value, so I prevent that here (you start with the dog)
    @Override
    protected void updateAttributesForTamed() {}

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createWolfAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6).add(EntityAttributes.GENERIC_MAX_HEALTH, 435.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 30.0).add(EntityAttributes.GENERIC_SCALE, 1.5).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64).add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 64);
    }
}
