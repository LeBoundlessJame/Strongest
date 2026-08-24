package com.boundless.entity.grapple;

import com.boundless.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GrappleEntity extends PersistentProjectileEntity {

    private static final double AIR_FRICTION = 0.995D;
    private static final double BOOST_X_MUL = 0.5D, BOOST_Y_MUL = 2.0D, BOOST_Z_MUL = 0.5D;

    private boolean attachedToBlock = false;
    private float ropeLength = 0.0F;
    private Vec3d prevPos = null;

    public GrappleEntity(LivingEntity owner, World world) {
        super(EntityRegistry.GRAPPLE, owner, world, Items.BLACK_WOOL.getDefaultStack(), Items.BLACK_WOOL.getDefaultStack());
        this.setNoGravity(true);
        this.setDamage(0);
    }

    public GrappleEntity(EntityType<? extends GrappleEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
        this.setDamage(0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getOwner() instanceof LivingEntity owner) {
            if (owner.isOnGround()) {
                owner.setNoDrag(false);
            }

            if (attachedToBlock) {
                swingLogic(owner);
            }

            if (this.age <= 2) {
                attachedToBlock = true;
                ropeLength = (float) (this.getPos().subtract(owner.getPos()).length() + 0.2F);
            }
        } else {
            discard();
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.WHITE_WOOL);
    }

    public void swingBoost(LivingEntity livingEntity) {
        livingEntity.setVelocity(livingEntity.getVelocity().multiply(BOOST_X_MUL, BOOST_Y_MUL, BOOST_Z_MUL));
    }

    public void swingLogic(LivingEntity livingEntity) {
        if (prevPos == null) {
            prevPos = livingEntity.getPos();
        }

        Vec3d currentPos = prevPos;
        Vec3d testPos = currentPos.add(livingEntity.getVelocity());

        double distance = testPos.subtract(this.getPos()).length();

        if (distance > ropeLength - 0.2D) {
            testPos = this.getPos().add(testPos.subtract(this.getPos()).normalize().multiply(ropeLength - 0.2D));
            Vec3d vel = testPos.subtract(currentPos);

            if (vel.length() < 500.0D) {
                livingEntity.setVelocity(vel);
            }
        }

        boolean isColliding = this.getWorld().getBlockCollisions(livingEntity, livingEntity.getBoundingBox().expand(0.1D)).iterator().hasNext();

        if (!isColliding) {
            livingEntity.setVelocity(livingEntity.getVelocity().multiply(AIR_FRICTION));

            if (!livingEntity.isOnGround()) {
                livingEntity.setNoDrag(true);
            }

            prevPos = testPos;
            livingEntity.setPosition(prevPos.x, prevPos.y, prevPos.z);
        } else {
            livingEntity.setNoDrag(false);
            prevPos = livingEntity.getPos();
        }

        if ((livingEntity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().flying) || isColliding) {
            livingEntity.setNoDrag(false);
        } else if (!livingEntity.isOnGround()) {
            livingEntity.setNoDrag(true);
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }
}