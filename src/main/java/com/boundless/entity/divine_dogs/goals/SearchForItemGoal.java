package com.boundless.entity.divine_dogs.goals;

import com.boundless.entity.divine_dogs.shiro.DivineDogShiroEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class SearchForItemGoal extends Goal {
    private static final int SEARCH_RANGE = 8;

    private final DivineDogShiroEntity shiro;

    public SearchForItemGoal(DivineDogShiroEntity shiro) {
        this.shiro = shiro;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (shiro.isHasItemToReturn()) return false;

        ItemStack heldItem = shiro.getEquippedStack(EquipmentSlot.MAINHAND);

        if (heldItem.isEmpty()) {
            return false;
        }

        if (shiro.getTarget() != null || shiro.getAttacker() != null) {
            return false;
        }

        if (shiro.getRandom().nextInt(toGoalTicks(10)) != 0) {
            return false;
        }

        return !findWantedItems().isEmpty();
    }

    @Override
    public boolean shouldContinue() {
        return !shiro.isHasItemToReturn() && !shiro.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty() && shiro.getTarget() == null && shiro.getAttacker() == null;
    }

    @Override
    public void start() {
        moveToItem();
    }

    @Override
    public void stop() {
        shiro.getNavigation().stop();
    }

    @Override
    public void tick() {
        moveToItem();
    }

    private void moveToItem() {
        List<ItemEntity> items = findWantedItems();

        if (!items.isEmpty()) {
            shiro.getNavigation().startMovingTo(items.getFirst(), 1.2F);
        } else {
            shiro.getNavigation().stop();
        }
    }

    private List<ItemEntity> findWantedItems() {
        return shiro.getWorld().getEntitiesByClass(ItemEntity.class, shiro.getBoundingBox().expand(SEARCH_RANGE), this::isWantedItem);
    }

    private boolean isWantedItem(ItemEntity item) {
        return item.isAlive() && !item.cannotPickup() && shiro.canGather(item.getStack());
    }
}