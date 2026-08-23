package com.boundless.entity.gama;

import com.boundless.entity.divine_dogs.kuro.DivineDogDispatcher;
import com.boundless.util.Shikigami;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.world.World;

public class GamaEntity extends FrogEntity implements Shikigami {
    public final GamaDispatcher dispatcher;

    public GamaEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new GamaDispatcher(this);
    }

    @Override
    public void tick() {
        super.tick();
        this.animationTick();
    }

    public void animationTick() {
        if (!this.getWorld().isClient) return;

        this.dispatcher.idle();
    }

    public static DefaultAttributeContainer.Builder createFrogAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)
                .add(EntityAttributes.GENERIC_SCALE, 2.0);
    }
}
