package com.boundless.entity.rock;

import com.boundless.BoundlessAPI;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.EffekUtils;
import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RockEntity extends PersistentProjectileEntity {
    private int maxLifetime = 40;

    private static final AzCommand SPIN_COMMAND = AzCommand.create("base_controller",
            "spin", AzPlayBehaviors.LOOP
    );

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    public RockEntity(EntityType<RockEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public RockEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.ROCK, world);
        this.setOwner(livingEntity);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age == 1) {
            SPIN_COMMAND.sendForEntity(this);
        }

        if (this.age > maxLifetime) {
            this.discard();
        }

        if (this.age % 2 == 0) {
            EffekUtils.playEffect(BoundlessAPI.identifier("energy_flicker"), this, this.getPos(), new Vec3d(1, 1, 1));
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof LivingEntity target) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 3, false, false, true));
        }

        if (this.getOwner() instanceof LivingEntity livingEntity) {
            livingEntity.onAttacking(entity);
        }
        if (entity.damage(this.getDamageSources().fallingBlock(this.getOwner()), (float) this.getDamage())) {
            this.playSound(SoundRegistry.EARTH_IMPACT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }
        this.setNoGravity(false);
        this.setVelocity(this.getVelocity().multiply(0.1, 1, 0.1));
        this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.getWorld().addBlockBreakParticles(this.getBlockPos(), this.getWorld().getBlockState(this.getBlockPos().down()));
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Blocks.STONE.asItem().getDefaultStack();
    }
}
