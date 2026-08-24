package com.boundless.entity.gama;

import com.boundless.entity.gama.goals.ToadLeapGoal;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.Shikigami;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class GamaEntity extends TameableEntity implements Shikigami, Tameable, JumpingMount {
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(GamaEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public final GamaDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;

    public GamaEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new GamaDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public GamaEntity(World world, PlayerEntity owner) {
        super(EntityRegistry.GAMA, world);
        this.dispatcher = new GamaDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.setOwnerUuid(owner.getUuid());
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient) return ActionResult.SUCCESS;
        if (this.hasPassengers() || this.getOwner() != player) return ActionResult.PASS;

        player.startRiding(this);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);

        Vec2f rotation = this.getControlledRotation(controllingPlayer);
        this.setRotation(rotation.y, rotation.x);
        this.prevYaw = this.bodyYaw = this.headYaw = this.getYaw();
    }

    private Vec2f getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2f(controllingPassenger.getPitch() * 0.5F, controllingPassenger.getYaw());
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float sideways = controllingPlayer.sidewaysSpeed * 0.5F;
        float forward = controllingPlayer.forwardSpeed;

        if (forward <= 0.0F) {
            forward *= 0.25F;
        }

        return new Vec3d(sideways, 0.0, forward);
    }

    @Override
    protected float getSaddledSpeed(PlayerEntity controllingPlayer) {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return getPassengerAttachmentPos(this, passenger, dimensions.attachments()).add(new Vec3d(0.0f, -0.4f * scaleFactor, -0.3f * scaleFactor).rotateY(-this.getYaw() * MathHelper.RADIANS_PER_DEGREE));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof PlayerEntity playerEntity) return playerEntity;
        return null;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new ToadLeapGoal(this));
        this.goalSelector.add(2, new SwimGoal(this));
        this.goalSelector.add(9, new FollowOwnerGoal(this, 1.0, 10.0F, 32.0f));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
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

    private void animationTick() {
        if (!this.getWorld().isClient) return;

        boolean isMovingOnGround = this.moveAnalysis.isMovingHorizontally() && this.isOnGround();

        if (isMovingOnGround) {
            this.dispatcher.walk();
        } else if (this.isOnGround()) {
            this.dispatcher.idle();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_FROG_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_FROG_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_FROG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_FROG_STEP, 0.15F, 1.0F);
    }

    @Nullable
    @Override
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(OWNER_UUID, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        UUID owner = this.getOwnerUuid();
        if (owner != null) {
            nbt.putUuid("Owner", owner);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            this.setOwnerUuid(nbt.getUuid("Owner"));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createFrogAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0).add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0).add(EntityAttributes.GENERIC_SCALE, 2.0);
    }

    @Override
    public void setJumpStrength(int strength) {
        this.leap(strength);
    }

    @Override
    public boolean canJump() {
        return this.isOnGround();
    }

    @Override
    public void startJumping(int height) {}

    @Override
    public void stopJumping() {}

    public void leap(float strength) {
        if (!this.isOnGround()) return;

        Vec3d velocity = this.getVelocity();
        float minJumpVelocity = 0.8f;
        float maxJumpVelocity = 1.6f;
        float verticalVelocity = MathHelper.clampedLerp(minJumpVelocity, maxJumpVelocity, strength / 100);
        float horizontalMultiplier = MathHelper.clampedLerp(1.0f, 3.2f, strength / 100);

        this.setVelocity(velocity.x * horizontalMultiplier, verticalVelocity, velocity.z * horizontalMultiplier);
        this.velocityDirty = true;
    }
}
