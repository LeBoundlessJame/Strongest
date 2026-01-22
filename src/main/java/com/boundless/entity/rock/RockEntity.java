package com.boundless.entity.rock;

import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.EntityRegistry;
import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RockEntity extends PersistentProjectileEntity {
    private static final AzCommand SPIN_COMMAND = AzCommand.create("base_controller",
            "spin", AzPlayBehaviors.LOOP
    );

    public RockEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.ROCK, world);
        this.setOwner(livingEntity);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public RockEntity(EntityType<RockEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age == 2) {
            SPIN_COMMAND.sendForEntity(this);
            System.out.println("Spinning!");
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Blocks.STONE.asItem().getDefaultStack();
    }
}
