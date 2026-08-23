package com.boundless.entity.gama;

import com.boundless.entity.gama.goals.ToadLeapGoal;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.Shikigami;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class GamaEntity extends TameableEntity implements Shikigami, Tameable {
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(GamaEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public final GamaDispatcher dispatcher;

    public GamaEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new GamaDispatcher(this);
    }

    public GamaEntity(World world, PlayerEntity owner) {
        super(EntityRegistry.GAMA, world);
        this.dispatcher = new GamaDispatcher(this);
        this.setOwnerUuid(owner.getUuid());
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
        this.animationTick();
    }

    private void animationTick() {
        if (!this.getWorld().isClient) return;

        if (this.isOnGround()) {
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

    public static DefaultAttributeContainer.Builder createFrogAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)
                .add(EntityAttributes.GENERIC_SCALE, 2.0)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 15f);
    }
}
