package com.boundless.entity.open;

import com.boundless.BoundlessAPI;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.hero.shrine_hero.ShrineHelper;
import com.boundless.hero.shrine_hero.ShrineHero;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.GameRulesRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.AOEUtils;
import com.boundless.util.CameraUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class OpenEntity extends PersistentProjectileEntity {

    public OpenEntity(EntityType<OpenEntity> entityType, World world) {
        super(entityType, world);
    }

    public OpenEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.OPEN_ENTITY, world);
        this.setOwner(livingEntity);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    // Todo: add age increment, + despawn after a while
    @Override
    public void tick() {
        super.tick();
        this.getWorld().addImportantParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 1, 0);
        this.getWorld().addImportantParticle(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 0, -1, 0);
    }

    // Todo: make it so that explosion doesn't do damage
    @Override
    public void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_upgraded"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);

        if (!this.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            if (serverWorld.getGameRules().getBoolean(GameRulesRegistry.TECHNIQUE_DESTRUCTION)) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 20f, true, World.ExplosionSourceType.BLOCK);
            }
            openDamage(ShrineHelper.getScaledDamage((PlayerEntity) this.getOwner(), ShrineHero.DAMAGE.weakestOpenDamage.get(), ShrineHero.DAMAGE.strongestOpenDamage.get()));
        }

        this.discard();
    }

    // Todo: make impact frame apply to each entity in the radius, make it radius based
    @Override
    public void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (result.getEntity() == null || this.getOwner() == null) return;

        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_upgraded"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);

        if (!this.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) this.getWorld();
            if (serverWorld.getGameRules().getBoolean(GameRulesRegistry.TECHNIQUE_DESTRUCTION)) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 20f, true, World.ExplosionSourceType.BLOCK);
            }
            openDamage(ShrineHelper.getScaledDamage((PlayerEntity) this.getOwner(), ShrineHero.DAMAGE.weakestOpenDamage.get(), ShrineHero.DAMAGE.strongestOpenDamage.get()));
        }

        this.discard();
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_FIRECHARGE_USE;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    public void openDamage(float amount) {
        CameraUtils.playCameraShake((PlayerEntity) this.getOwner());
        SoundUtils.playSound((PlayerEntity) this.getOwner(), SoundEvents.ITEM_FIRECHARGE_USE, 5, 8);

        AOEUtils.forEach(this, 20, (open, target) -> {
            if (target instanceof PlayerEntity player) CameraUtils.playCameraShake(player);
            if (target != this.getOwner()) {
                target.timeUntilRegen = 0;
                target.damage(this.getDamageSources().lava(), amount);
                target.setOnFireFor(10);
            }
        });
    }
}
