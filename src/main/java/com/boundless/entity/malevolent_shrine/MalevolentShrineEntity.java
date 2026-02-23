package com.boundless.entity.malevolent_shrine;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.SoundRegistry;
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
    public void tick() {
        this.setInvisible(age == 0);
        super.tick();

        if (age >= maxLifetime || (!this.getWorld().isClient && this.getOwner() == null || (this.getOwner() != null && !this.getOwner().isAlive()))) {
            destroySurehitEffect();
            this.discard();
        }

        if (this.getOwner() == null) return;

        entitiesInRange.forEach(entity -> {
            entity.timeUntilRegen = 0;
            entity.damage(entity.getDamageSources().generic(), 1);
        });

        if (age % 20 == 0) {
            entitiesInRange.clear();
            entitiesInRange.addAll(this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(domainRadius.getX(), domainRadius.getY(), domainRadius.getZ()), entity -> true));
        }

        if (age == 0) {
           if (this.getWorld().isClient) {
               dispatcher.domainBegin();
           }
        }

        if (age == delay) {
            bindSurehitEffect(this.getPos(), this.getScale());
        }

        if (this.age % 20 == 0) {
            // Todo: maybe make it so that you can tweak domain x, y , z via binding vow
            // Todo: reinstate custom damage amount
            for (LivingEntity livingEntity : this.getWorld().getEntitiesByClass(LivingEntity.class, new Box(this.getBlockPos()).expand(domainRadius.getX(), domainRadius.getY() / 2, domainRadius.getZ()), entity -> true)) {
                if (livingEntity != this.getOwner()) {
                    livingEntity.timeUntilRegen = 0;
                    livingEntity.damage(livingEntity.getDamageSources().magic(), this.getDamagePerSlash());
                    livingEntity.timeUntilRegen = 0;
                }
            }
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
            emitter.ifPresent(particleEmitter -> particleEmitter.sendTrigger(0));
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        destroySurehitEffect();
        this.setRemoved(reason);
    }
}
