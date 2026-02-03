package com.boundless.entity.malevolent_shrine;

import com.boundless.registry.EntityRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Setter
@Getter
public class MalevolentShrineEntity extends Entity implements Ownable {
    public final MalevolentShrineDispatcher dispatcher;

    public int maxLifetime = 1200;
    public int age;

    @Override
    public void tick() {
        this.setInvisible(age == 0);
        super.tick();
        if (age == 0 && this.getWorld().isClient) {
           dispatcher.domainBegin();
        }

        if (age >= maxLifetime || (!this.getWorld().isClient && this.getOwner() == null || (this.getOwner() != null && !this.getOwner().isAlive()))) {
            this.discard();
        }
        age++;
    }

    public MalevolentShrineEntity(EntityType<?> type, World world) {
        super(type, world);
        this.dispatcher = new MalevolentShrineDispatcher(this);
        this.setInvisible(true);
    }

    public MalevolentShrineEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.MALEVOLENT_SHRINE, world);
        this.dispatcher = new MalevolentShrineDispatcher(this);
        this.setOwner(livingEntity);
        this.setInvisible(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    @Nullable @Setter
    private Entity owner;
    @Nullable @Setter @Getter
    private UUID ownerUuid;

    @Nullable
    @Override
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        } else if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld serverWorld) {
            this.owner = serverWorld.getEntity(this.ownerUuid);
            return this.owner;
        } else {
            return null;
        }
    }
}
