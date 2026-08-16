package com.boundless.entity.divine_dogs.goals;

import com.boundless.entity.divine_dogs.kuro.DivineDogShiroEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public class ReturnFoundItemGoal extends Goal {
    private static final double GIVE_DISTANCE = 3.0D;

    private final DivineDogShiroEntity shiro;

    public ReturnFoundItemGoal(DivineDogShiroEntity shiro) {
        this.shiro = shiro;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return shiro.isHasItemToReturn()
                && shiro.getOwner() instanceof PlayerEntity
                && !shiro.getInventory().isEmpty();
    }

    @Override
    public boolean shouldContinue() {
        return shiro.isHasItemToReturn()
                && shiro.getOwner() instanceof PlayerEntity
                && !shiro.getInventory().isEmpty();
    }

    @Override
    public void start() {
        if (!(shiro.getOwner() instanceof PlayerEntity owner)) return;
        shiro.getNavigation().startMovingTo(owner, 1.2D);
    }

    @Override
    public void tick() {
        if (!(shiro.getOwner() instanceof PlayerEntity owner)) return;

        if (shiro.squaredDistanceTo(owner) <= GIVE_DISTANCE * GIVE_DISTANCE) {
            giveItems(owner);
        } else {
            shiro.getNavigation().startMovingTo(owner, 1.2D);
        }
    }

    @Override
    public void stop() {
        shiro.getNavigation().stop();
    }

    private void giveItems(PlayerEntity owner) {
        for (ItemStack stack : shiro.getInventory().clearToList()) {
            if (!stack.isEmpty()) {
                owner.giveItemStack(stack);
            }
        }

        shiro.setHasItemToReturn(false);
    }
}