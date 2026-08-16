package com.boundless.entity.divine_dogs.goals;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class SearchForItemGoal extends Goal {
    private static final int SEARCH_RANGE = 8;

    private final MobEntity mob;

    public SearchForItemGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        ItemStack heldItem = mob.getEquippedStack(EquipmentSlot.MAINHAND);

        if (heldItem.isEmpty()) {
            return false;
        }

        if (mob.getTarget() != null || mob.getAttacker() != null) {
            return false;
        }

        if (mob.getRandom().nextInt(toGoalTicks(10)) != 0) {
            return false;
        }

        return !findWantedItems().isEmpty();
    }

    @Override
    public boolean shouldContinue() {
        return !mob.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()
                && mob.getTarget() == null
                && mob.getAttacker() == null;
    }

    @Override
    public void start() {
        moveToItem();
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        moveToItem();
    }

    private void moveToItem() {
        List<ItemEntity> items = findWantedItems();

        if (!items.isEmpty()) {
            mob.getNavigation().startMovingTo(items.getFirst(), 1.2F);
        } else {
            mob.getNavigation().stop();
        }
    }

    private List<ItemEntity> findWantedItems() {
        return mob.getWorld().getEntitiesByClass(
                ItemEntity.class,
                mob.getBoundingBox().expand(SEARCH_RANGE),
                this::isWantedItem
        );
    }

    private boolean isWantedItem(ItemEntity item) {
        return item.isAlive()
                && !item.cannotPickup()
                && mob.canGather(item.getStack());
    }
}