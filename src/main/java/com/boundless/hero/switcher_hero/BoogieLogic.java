package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.rock.RockEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class BoogieLogic {

    /*
    public static void boogie(PlayerEntity user) {
        if (user.getWorld().isClient()) return;

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("clap"), 1.0f, false, true, 3000);

        BiConsumer<PlayerEntity, HeroActionEntity> teleport = (player, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.CLAP_1, 8, 12);

            Entity target = RaycastUtils.thickRaycast(player, 64, 1.5f);
            if (target == null || target == player) return;


            Vec3d playerPos = player.getPos();
            Vec3d targetPos = target.getPos();

            target.requestTeleport(playerPos.x, playerPos.y, playerPos.z);
            player.requestTeleport(targetPos.x, targetPos.y, targetPos.z);

            EffekUtils.playVisual(player, BoundlessAPI.identifier("energy_spark"));
            if (target instanceof LivingEntity livingEntity) {
                EffekUtils.playVisual(livingEntity, BoundlessAPI.identifier("energy_spark"));
            }

            player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, playerPos);
        };

        tasks.put(5, teleport);
        ActionUtils.performAction(user, Action.builder().scheduledTasks(tasks).build());
    }

     */

    public static void boogie(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        RockEntity rock = new RockEntity(player, player.getWorld());
        rock.setVelocity(player.getRotationVector().multiply(2));
        rock.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1.2, player.getRotationVector().multiply(2).z));
        rock.setNoGravity(true);
        rock.setPitch(player.getPitch());
        rock.setYaw(player.getYaw());
        rock.setGlowing(true);
        player.getWorld().spawnEntity(rock);
        //rock.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
    }
}
