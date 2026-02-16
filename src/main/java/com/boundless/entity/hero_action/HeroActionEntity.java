package com.boundless.entity.hero_action;

import com.boundless.action.Action;
import com.boundless.entity.ModEntityDimensions;
import com.boundless.registry.EntityRegistry;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class HeroActionEntity extends PersistentProjectileEntity {
    @Getter @Setter
    private BiConsumer<PlayerEntity, HeroActionEntity> customTickLogic;

    @Getter @Setter
    private int lifetime = 0;
    @Setter
    private int maxLifetime = 20;
    private float remainingActions = 1;
    private LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> scheduledTasks;

    private static final TrackedData<Float> WIDTH_X = DataTracker.registerData(HeroActionEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(HeroActionEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WIDTH_Z = DataTracker.registerData(HeroActionEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<String> RENDER_LOGIC_ID = DataTracker.registerData(HeroActionEntity.class, TrackedDataHandlerRegistry.STRING);

    public HeroActionEntity(LivingEntity livingEntity, World world, Action action) {
        super(EntityRegistry.HERO_ACTION_ENTITY, world);
        this.setOwner(livingEntity);
        this.calculateBoundingBox();
        this.reinitDimensions();
        this.customTickLogic = action.customTickLogic;
        this.scheduledTasks = new LinkedHashMap<>(action.scheduledTasks);
        this.remainingActions = action.scheduledTasks.size();
        this.pickupType = PickupPermission.DISALLOWED;
        this.dataTracker.set(RENDER_LOGIC_ID, action.renderLogicID);
    }

    public HeroActionEntity(EntityType<HeroActionEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(WIDTH_X, 2.0f);
        builder.add(HEIGHT, 2.0f);
        builder.add(WIDTH_Z, 2.0f);
        builder.add(RENDER_LOGIC_ID, "");
    }

    public void setSize(float widthX, float height, float widthZ) {
        this.dataTracker.set(WIDTH_X, widthX);
        this.dataTracker.set(HEIGHT, height);
        this.dataTracker.set(WIDTH_Z, widthZ);
        this.refreshPosition();
        this.calculateDimensions();
    }

    @Override
    protected Box calculateBoundingBox() {
        if (this.getOwner() == null) {
            return ModEntityDimensions.getBoxAt(new ModEntityDimensions(4.0f, 2.0f, 2.0f), this.getPos());
        }

        Vec3d origin = this.getPos();
        Vec3d rotationVec = this.getOwner().getRotationVector();
        Vec3d forward = new Vec3d(rotationVec.x, 0, rotationVec.z).normalize();

        return ModEntityDimensions.getOrientedBoxAt(origin, forward, this.dataTracker.get(WIDTH_X), this.dataTracker.get(HEIGHT), this.dataTracker.get(WIDTH_Z));
    }


    @Override
    public void tick() {
        if (lifetime >= maxLifetime || this.getOwner() == null) this.discard();
        scheduledActionsCheck();
        super.tick();

        if (customTickLogic != null && this.getOwner() instanceof PlayerEntity player) {
            customTickLogic.accept(player, this);
        }

        if (!this.getWorld().isClient) {
            lifetime++;
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    private void scheduledActionsCheck() {
        if (scheduledTasks == null) return;
        repositionBox();

        if (remainingActions > 0 && !scheduledTasks.isEmpty() && lifetime == scheduledTasks.keySet().stream().findFirst().get() && this.getOwner() != null && this.getOwner().isAlive()) {
            scheduledTasks.get(lifetime).accept((PlayerEntity) this.getOwner(), this);
            scheduledTasks.remove(lifetime);
            remainingActions--;
        }
    }

    public void repositionBox() {
        if (this.getOwner() == null) return;
        Vec3d rotationVector = this.getOwner().getRotationVector().normalize();
        this.setPos(this.getOwner().getX() + rotationVector.multiply(0.5).x, this.getOwner().getY(), this.getOwner().getZ() + rotationVector.multiply(0.5).z);
    }
}