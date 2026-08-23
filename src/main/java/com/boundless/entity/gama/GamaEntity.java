package com.boundless.entity.gama;

import com.boundless.entity.divine_dogs.kuro.DivineDogDispatcher;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.Shikigami;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
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
    public void tick() {
        super.tick();
        this.animationTick();
    }

    private void animationTick() {
        if (!this.getWorld().isClient) return;
        this.dispatcher.idle();
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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)
                .add(EntityAttributes.GENERIC_SCALE, 2.0);
    }
}
