package com.boundless.entity.malevolent_shrine;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.SoundUtils;
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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
public class MalevolentShrineEntity extends Entity implements Ownable {
    public final MalevolentShrineDispatcher dispatcher;

    public int maxLifetime = 1200;
    public int age;
    public float scale = 1f;
    public int delay;
    public Vec3d domainRadius = new Vec3d(100, 100, 100);
    public int damagePerSlash = 1;
    public HashSet<LivingEntity> entitiesInRange = new HashSet<>();

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public void tick() {
        this.setInvisible(age == 0);
        super.tick();

        if (this.getAge() == 0 && this.getWorld().isClient) {
            this.dispatcher.domainBegin();
        }

        if (this.getAge() > this.getMaxLifetime()) {
            this.discard();
        }

        if (this.getAge() % 20 == 0) {
            entitiesInRange.clear();
            entitiesInRange.addAll(this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(domainRadius.getX(), domainRadius.getY(), domainRadius.getZ()), entity -> entity != this.getOwner()));
        }

        if (this.getAge() > delay && !this.getWorld().isClient) {
            entitiesInRange.forEach(entity -> {
                entity.timeUntilRegen = 0;
                entity.damage(entity.getDamageSources().generic(), this.getDamagePerSlash());

                if (entity.age % 5 == 0 && entity.isAlive()) {
                    EffekUtils.playRandomRotatedEffect(BoundlessAPI.identifier("upgraded_dismantle"), entity, entity.getPos().add(0, entity.getHeight() / 2, 0), new Vec3d(1, 1, 1));
                }
            });
        }

        if (this.getAge() % 100 == 0) {
            bindSurehitEffect(this.getPos(), 15f);
        }

        if (this.getAge() >= delay && this.getAge() % 20 == 0) {
            applyShrineShaderInRadius();
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

    public void bindSurehitEffect(Vec3d pos, float scale) {
        ParticleEmitterInfo particleEmitter = ParticleEmitterInfo.create(this.getWorld(), BoundlessAPI.identifier("optimised_shrine"), Identifier.of(BoundlessAPI.MOD_ID, "optimised_shrine" + this.getId()));
        particleEmitter.position(pos);
        particleEmitter.scale(scale);
        AAALevel.addParticle(this.getWorld(), true, particleEmitter);
    }

    public void destroySurehitEffect() {
        var effect = EffectRegistry.get(BoundlessAPI.identifier("optimised_shrine"));

        if (effect != null) {
            Optional<ParticleEmitter> emitter = effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessAPI.identifier("optimised_shrine" + this.getId()));
            emitter.ifPresent(particleEmitter -> particleEmitter.stop());
        }
    }

    public void applyShrineShaderInRadius() {
        for (PlayerEntity playerEntity: this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoundingBox().expand(domainRadius.getX(), domainRadius.getY(), domainRadius.getZ()), entity -> true)) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SHRINE_EFFECT, 21, 0, false, false, false));
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        destroySurehitEffect();
        this.setRemoved(reason);
    }
}
