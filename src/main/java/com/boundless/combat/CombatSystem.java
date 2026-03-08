package com.boundless.combat;

import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class CombatSystem {
    public static void moveToPos(PlayerEntity player, Vec3d target, int maxTicks, double maxSpeed) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        for (int i = 0; i < maxTicks; i++) {
            tasks.put(i, (user, action) -> {
                if (action.isCancelled()) return;

                Vec3d toTarget = target.subtract(user.getPos());

                if (toTarget.lengthSquared() < 0.5) {
                    user.setVelocity(Vec3d.ZERO);
                    user.velocityModified = true;
                    player.sendMessage(Text.of("Got close enough fr fr"));
                    action.setCancelled(true);
                    return;
                }

                Vec3d velocity = toTarget.normalize().multiply(maxSpeed);

                if (toTarget.length() < maxSpeed) {
                    velocity = toTarget;
                }

                user.setVelocity(velocity);
                user.velocityModified = true;

                user.fallDistance = 0;
            });
        }

        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, maxTicks);
    }
}
