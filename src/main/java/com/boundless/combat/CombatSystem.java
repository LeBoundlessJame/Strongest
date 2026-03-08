package com.boundless.combat;

import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.shrine_hero.ShrineHeroMelee;
import com.boundless.util.ActionUtils;
import com.boundless.util.AttackUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class CombatSystem {
    public static void moveToEntity(PlayerEntity player, float range, int maxTicks, double maxSpeed) {
        EntityHitResult result = RaycastUtils.raycast(player, range);
        Entity entity = null;
        if (result == null) {
            entity = RaycastUtils.thickRaycast(player, range, 1.5f);
        } else {
            entity = result.getEntity();
        }
        if (entity == null) return;

        moveToPos(player, entity.getPos(), maxTicks, maxSpeed, 4);
    }

    // 1 is a good default for distanceToEntity for this but I prefer 5
    public static void moveToPos(PlayerEntity player, Vec3d target, int maxTicks, double maxSpeed, float distanceFromEntity) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        for (int i = 0; i < maxTicks; i++) {
            tasks.put(i, (user, action) -> {
                if (action.isCancelled()) return;

                Vec3d toTarget = target.subtract(user.getPos());

                if (toTarget.length() < distanceFromEntity) {
                    action.setCancelled(true);
                    user.setVelocity(Vec3d.ZERO);
                    user.velocityModified = true;
                    ShrineHeroMelee.lightAttack(user);
                    AttackUtils.startAttackTimer(player, 10);
                    return;
                }

                Vec3d velocity = toTarget.normalize().multiply(0.75);
                velocity = velocity.multiply(Math.min(toTarget.length(), maxSpeed));

                if (toTarget.length() < maxSpeed) {
                    velocity = toTarget;
                }

                user.setVelocity(velocity);
                user.velocityModified = true;

                user.fallDistance = 0;
            });
        }

        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        //AttackUtils.startAttackTimer(player, maxTicks);
    }
}
