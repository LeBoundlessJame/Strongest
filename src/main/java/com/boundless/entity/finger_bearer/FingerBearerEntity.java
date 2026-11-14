package com.boundless.entity.finger_bearer;

import com.boundless.entity.divine_dogs.kuro.DivineDogDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class FingerBearerEntity extends HostileEntity {
    public final FingerBearerDispatcher dispatcher;

    public FingerBearerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new FingerBearerDispatcher(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.dispatcher.chargeUp();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 435.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.2)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 64);
    }
}
