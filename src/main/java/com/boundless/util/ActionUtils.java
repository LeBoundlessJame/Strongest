package com.boundless.util;

import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;

// Todo: Might fully remove this class at some point, gotta think over if I need it for rendering or something
public class ActionUtils {

    public static Action singleAction(int taskTick, BiConsumer<PlayerEntity, HeroActionEntity> taskLogic) {
        return singleAction(taskTick, taskLogic, 4, 2, 2);
    }

    public static Action singleAction(int taskTick, BiConsumer<PlayerEntity, HeroActionEntity> taskLogic, float widthX, float height, float widthZ) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> scheduledTasks = new LinkedHashMap<>();
        scheduledTasks.put(taskTick, taskLogic);
        return Action.builder().scheduledTasks(scheduledTasks).hitboxWidthX(widthX).hitboxHeight(height).hitboxWidthZ(widthZ).build();
    }

    public static void performAction(PlayerEntity player, Action action) {
        List<Integer> keys = new ArrayList<>(action.scheduledTasks.keySet());
        int lifetime = keys.getLast();

        HeroActionEntity heroAction = ActionUtils.createHeroAction(player, action, lifetime);
        ActionUtils.summonHeroAction(player, heroAction);
    }

    public static HeroActionEntity createHeroAction(PlayerEntity user, Action action, int lifetime) {
        HeroActionEntity heroAction = new HeroActionEntity(user, user.getWorld(), action);
        heroAction.setSize(action.getHitboxWidthX(), action.getHitboxHeight(), action.getHitboxWidthZ());
        heroAction.setOwner(user);
        heroAction.setMaxLifetime(lifetime + 1);
        heroAction.setNoGravity(true);
        heroAction.setPos(user.getX(), user.getY(), user.getZ());
        return heroAction;
    }

    private static void summonHeroAction(PlayerEntity user, HeroActionEntity heroAction) {
        user.getWorld().spawnEntity(heroAction);
    }
}
