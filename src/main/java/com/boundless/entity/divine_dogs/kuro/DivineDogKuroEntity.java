package com.boundless.entity.divine_dogs.kuro;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.goals.KuroPounceAtTargetGoal;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.EffekUtils;
import dev.kosmx.playerAnim.core.util.Vec3f;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DivineDogKuroEntity extends WolfEntity {
    public final DivineDogKuroDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;

    public DivineDogKuroEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new DivineDogKuroDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public DivineDogKuroEntity(World world, PlayerEntity owner) {
        super(EntityRegistry.DIVINE_DOG_KURO, world);
        this.dispatcher = new DivineDogKuroDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.setOwner(owner);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new TameableEntity.TameableEscapeDangerGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
        this.goalSelector.add(2, new SitGoal(this));
        //this.goalSelector.add(4, new KuroPounceAtTargetGoal(this, 1f));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0f));
        this.goalSelector.add(7, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(9, new WolfBegGoal(this, 8.0F));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, (entity) -> {
            return this.shouldAngerAt((LivingEntity) entity);
        }));
        this.targetSelector.add(5, new UntamedActiveTargetGoal(this, AnimalEntity.class, false, FOLLOW_TAMED_PREDICATE));
        this.targetSelector.add(6, new UntamedActiveTargetGoal(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
        this.targetSelector.add(7, new ActiveTargetGoal(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.add(8, new UniversalAngerGoal<>(this, true));
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
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45F).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0).add(EntityAttributes.GENERIC_SCALE, 1.5).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64).add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 64);
    }
}
