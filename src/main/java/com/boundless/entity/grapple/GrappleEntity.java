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

        if (this.getOwner() instanceof PlayerEntity player) {
            if (player.isOnGround()) {
                player.setNoDrag(false);
            }

            if (attachedToBlock) {
                swingLogic(player);
            }

            if (this.age <= 2) {
                attachedToBlock = true;
                ropeLength = (float) (this.getPos().subtract(player.getPos()).length() + 0.2F);
            }
        } else {
            discard();
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.WHITE_WOOL);
    }

    public void swingBoost(PlayerEntity player) {
        player.setVelocity(player.getVelocity().multiply(BOOST_X_MUL, BOOST_Y_MUL, BOOST_Z_MUL));
    }

    public void swingLogic(PlayerEntity player) {
        if (prevPos == null) {
            prevPos = player.getPos();
        }

        Vec3d currentPos = prevPos;
        Vec3d testPos = currentPos.add(player.getVelocity());

        double distance = testPos.subtract(this.getPos()).length();

        if (distance > ropeLength - 0.2D) {
            testPos = this.getPos().add(testPos.subtract(this.getPos()).normalize().multiply(ropeLength - 0.2D));
            Vec3d vel = testPos.subtract(currentPos);

            if (vel.length() < 500.0D) {
                player.setVelocity(vel);
            }
        }

        boolean isColliding = this.getWorld().getBlockCollisions(player, player.getBoundingBox().expand(0.1D)).iterator().hasNext();

        if (!isColliding) {
            player.setVelocity(player.getVelocity().multiply(AIR_FRICTION));

            if (!player.isOnGround()) {
                player.setNoDrag(true);
            }

            prevPos = testPos;
            player.setPosition(prevPos.x, prevPos.y, prevPos.z);
        } else {
            player.setNoDrag(false);
            prevPos = player.getPos();
        }

        if (player.getAbilities().flying || isColliding) {
            player.setNoDrag(false);
        } else if (!player.isOnGround()) {
            player.setNoDrag(true);
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }
}