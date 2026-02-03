package com.boundless.entity.open;

import com.boundless.BoundlessAPI;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.CameraUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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

    @Override
    public void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_upgraded"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);
        this.discard();
    }
    @Override
    public void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (result.getEntity() == null || !((result.getEntity()) instanceof LivingEntity livingEntity) || this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_upgraded"), livingEntity, livingEntity.getPos().add(0f, 0.1f, 0f), 0.5f);
        livingEntity.timeUntilRegen = 0;
        livingEntity.damage(livingEntity.getDamageSources().generic(), 500f);
        livingEntity.setOnFireFor(10);
        CameraUtils.playCameraShake((PlayerEntity) this.getOwner());
        SoundUtils.playSound((PlayerEntity) this.getOwner(), SoundEvents.ITEM_FIRECHARGE_USE, 5, 8);
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
}
