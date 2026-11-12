package com.boundless.entity.nail;

import com.boundless.BoundlessAPI;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.GameRulesRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NailEntity extends PersistentProjectileEntity {
    int MAX_AGE = 1200;

    public NailEntity(EntityType<NailEntity> entityType, World world) {
        super(entityType, world);
    }

    public NailEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.NAIL, world);
        this.setOwner(livingEntity);
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        age++;

        if (this.age >= MAX_AGE) {
            this.discard();
        }
    }

    public void detonate() {
        if (this.getOwner() == null) return;

        System.out.println("Boom");
        SoundUtils.playSound((LivingEntity) this.getOwner(), SoundRegistry.RATIO_IMPACT_1, 10, 11);
        EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("hairpin_impact"), this, this.getPos().add(0, this.getHeight() / 2, 0), new Vec3d(0.35, 0.35, 0.35));
        EffekUtils.playEffect(BoundlessAPI.id("dismantle_impact"), this, this.getPos().add(0, this.getHeight() / 2, 0), this.getHeight() / 16);
    }

    @Override
    public void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.id("energy_spark"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);
    }

    @Override
    public void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (result.getEntity() == null || this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.id("energy_spark"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);
        //this.discard();
    }

    protected SoundEvent getHitSound() {
        return SoundRegistry.RATIO_IMPACT_1;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }
}