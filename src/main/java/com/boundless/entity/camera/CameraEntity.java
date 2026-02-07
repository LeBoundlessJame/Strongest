package com.boundless.entity.camera;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CameraEntity extends Entity implements Ownable {
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    @Getter @Setter
    private int maxLifetime = 20;

    // Todo: using an armor stand is wild. rework this!
    public CameraEntity(EntityType<?> type, World world) {
        super(EntityType.ARMOR_STAND, world);
        this.setInvisible(true);
    }

    public CameraEntity(LivingEntity livingEntity, World world) {
        super(EntityType.ARMOR_STAND, world);
        this.setOwner(livingEntity);
        this.setInvisible(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= maxLifetime || this.getOwner() == null || !this.getOwner().isAlive()) {
            this.discard();
        }
        age++;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
            this.owner = null;
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }

    @Override
    public @Nullable Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld serverWorld) {
            this.owner = serverWorld.getEntity(this.ownerUuid);
            return this.owner;
        } else {
            return null;
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (this.getOwner() instanceof PlayerEntity player && HeroUtils.isHero(player)) {
            HeroUtils.getHeroStack(player).set(DataComponentRegistry.BOUND_CAMERA_ID, null);
        }
        this.setRemoved(reason);
    }
}
