package com.boundless.entity.divine_dogs.kuro;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;

import java.util.logging.Logger;

public class DivineDogKuroEntity extends WolfEntity {
    public final DivineDogKuroDispatcher dispatcher;

    public DivineDogKuroEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new DivineDogKuroDispatcher(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.animationTick();
    }

    public void animationTick() {
        if (this.isInSittingPose()) {
            this.dispatcher.layBegin();
        }
    }


    public static DefaultAttributeContainer.Builder createWolfAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6F)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0);
    }
}
