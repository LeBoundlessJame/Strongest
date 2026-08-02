package com.boundless.entity.divine_dogs.kuro;

import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.world.World;

public class DivineDogKuroEntity extends WolfEntity {
    public final DivineDogKuroDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;

    public DivineDogKuroEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new DivineDogKuroDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        this.animationTick();
    }

    public void animationTick() {
        if (!this.getWorld().isClient) return;

        if (!this.isInSittingPose()) {
            boolean isMovingOnGround = this.moveAnalysis.isMovingHorizontally() && this.isOnGround();

            if (isMovingOnGround) {
                if (this.hasAngerTime()) {
                    this.dispatcher.run();
                    System.out.println("Angry boi");
                } else {
                    System.out.println("Calm boi");
                    this.dispatcher.walk();
                }
            } else {
                this.dispatcher.idle();
            }
        } else {
            this.dispatcher.layIdle();
        }
    }


    public static DefaultAttributeContainer.Builder createWolfAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.45F).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0);
    }
}
