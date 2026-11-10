package com.boundless.entity.malevolent_shrine;


import com.boundless.BoundlessAPI;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import com.boundless.util.EffekUtils;
import lombok.Getter;
import lombok.Setter;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Setter
@Getter
public class MalevolentShrineEntity extends Entity implements Ownable {
    public final MalevolentShrineDispatcher dispatcher;

    public int maxLifetime = 200;
    public int delay = 20;
    // Todo: make this a config in a future update
    public Vec3d domainRadius = new Vec3d(200, 200, 200);
    public HashSet<LivingEntity> entitiesInRange = new HashSet<>();
    public boolean furnaceNukeActive = false;
    public int furnaceNukeTicks = 0;
    public int furnaceNukeDuration = 100;

    // I made this a function just in case the owner doesn't exist by the time this variable is initialized
    public float getDamagePerSlash() {
        if (this.getOwner() == null) return 0;
        // Todo: reinstate shrinehelper
        return 16;
        //return ShrineHelper.getScaledDamage((PlayerEntity) this.getOwner(), ShrineHero.DOMAIN.weakestSlashDamage.get(), ShrineHero.DOMAIN.strongestSlashDamage.get());
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
    public void tick() {
        this.setInvisible(this.age == 0);
        if (this.age == 0 && this.getWorld().isClient) {
            this.dispatcher.domainBegin();
        }
        super.tick();

        if (this.age > this.getMaxLifetime()) {
            this.discard();
        }

        if (this.age % 5 == 0) {
            shrineEffects();
        }

        if (this.furnaceNukeActive) {
            if (furnaceNukeTicks < furnaceNukeDuration) {
                furnaceNukeTicks++;
            } else {
                furnaceNukeActive = false;
            }
        } else {
            shrineLogic();
        }
    }

    public void applyShrineShaderInRadius() {
        if (this.getWorld().isClient) return;
        for (PlayerEntity playerEntity : this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(domainRadius.getX(), domainRadius.getY(), domainRadius.getZ()), entity -> true)) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SHRINE_EFFECT, 10, 0, false, false, false));
        }
    }

    public void initiateFurnaceNuke() {
        furnaceNukeTicks = 0;
        furnaceNukeActive = true;

        for (PlayerEntity playerEntity : this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(this.domainRadius.getX(), this.domainRadius.getY(), this.domainRadius.getZ()), entity -> true)) {
            playerEntity.removeStatusEffect(StatusEffectRegistry.GRAYSCALE);
            playerEntity.removeStatusEffect(StatusEffectRegistry.SHRINE_EFFECT);
        }
    }

    public void shrineEffects() {
        // Sometimes my optimization genius is almost frightening
        List<Vec3d> positions = new ArrayList<>();
        int vfxCount = 15;

        while (positions.size() < vfxCount) {
            int x = this.getRandom().nextBetween((int) -domainRadius.x, (int) domainRadius.x);
            int y = this.getRandom().nextBetween((int) -domainRadius.y, (int) domainRadius.y);
            int z = this.getRandom().nextBetween((int) -domainRadius.z, (int) domainRadius.z);

            positions.add(new Vec3d(x, y, z));
        }

        for (Vec3d pos: positions) {
            EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("upgraded_dismantle"), this, pos, new Vec3d(4, 4, 4));
        }
    }

    public void shrineLogic() {
        if (this.age >= delay && this.age % 5 == 0) {
            applyShrineShaderInRadius();
        }

        if (!this.getWorld().isClient && this.age >= this.getDelay() && this.age % 100 == 0) {
            //bindSurehitEffect(this.getPos(), 15f);
        }

        //        if (this.age % ShrineHero.DOMAIN.timeBetweenMobChecks.get() == 0) {
        if (this.age % 10 == 0) {
            entitiesInRange.clear();
            entitiesInRange.addAll(this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(domainRadius.getX(), domainRadius.getY(), domainRadius.getZ()), entity -> entity != this.getOwner()));
        }

        if (this.age > delay && !this.getWorld().isClient) {
            entitiesInRange.forEach(entity -> {
                //                if (entity.age % ShrineHero.DOMAIN.timeBetweenSlashes.get() == 0) {
                if (entity.age % 5 == 0) {
                    entity.timeUntilRegen = 0;
                    entity.damage(DamageTypeRegistry.getDamageSource(entity, DamageTypeRegistry.SHRINE_SLASHES), getDamagePerSlash());
                }

                if (entity.age % 5 == 0 && entity.isAlive()) {
                    EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("upgraded_dismantle"), entity, entity.getPos().add(0, entity.getHeight() / 2, 0), new Vec3d(1, 1, 1));
                }
            });
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.setRemoved(reason);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    @Nullable
    @Setter
    private Entity owner;
    @Nullable
    @Setter
    @Getter
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