package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.ActionUtils;
import com.boundless.util.AnimationUtils;
import com.boundless.util.AttackUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class CombatSystem {
    public static Entity getRaycastEntity(PlayerEntity player, float range, float radius) {
        EntityHitResult result = RaycastUtils.raycast(player, range);
        return result == null ? RaycastUtils.thickRaycast(player, range, radius) : result.getEntity();
    }

    public static void moveToEntity(PlayerEntity player, float range, int maxTicks, double maxSpeed, float distanceFromEntity) {
        Entity entity = getRaycastEntity(player, range, 1.5f);
        if (entity == null) return;

        moveToPos(player, entity.getPos(), maxTicks, maxSpeed, distanceFromEntity);
    }

    // 1 is a good default for distanceToEntity for this but I prefer 5
    public static void moveToPos(PlayerEntity player, Vec3d target, int maxTicks, double maxSpeed, float distanceFromEntity) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        for (int i = 0; i < maxTicks; i++) {
            tasks.put(i, (user, action) -> {
                if (action.isCancelled()) return;

                Vec3d toTarget = target.subtract(user.getPos());

                /*
                // Todo: make damage configurable, same damage as light attack
                if (!action.getEntitiesInBox().isEmpty()) {
                    action.getEntitiesInBox().forEach((entity) -> {
                        AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("hook"));
                        MeleeAbilities.basicPerEnemyLogic(user, entity, 14, 255, 8);
                        entity.damage(entity.getDamageSources().playerAttack(user), 20);
                        if (entity instanceof PlayerEntity targetPlayer) {
                            MeleeUtils.disorient(targetPlayer, 8);
                        }
                    });
                    MeleeUtils.disorient(player, 5);
                    AttackUtils.startAttackTimer(player, 10);
                    action.setCancelled(true);
                    return;
                }

                 */

                if (toTarget.length() < distanceFromEntity) {
                    action.setCancelled(true);
                    //user.setVelocity(Vec3d.ZERO);
                    //user.velocityModified = true;
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

        tasks.put(maxTicks + 1, (user, action) -> {
            if (AttackUtils.canAttack(user)) {
                AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("idle"));
            }
        });

        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        //AttackUtils.startAttackTimer(player, maxTicks);
    }
}
