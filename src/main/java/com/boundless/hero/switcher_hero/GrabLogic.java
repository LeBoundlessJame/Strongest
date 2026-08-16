package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Grab;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.WorldEvents;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class GrabLogic {

    public static void suplex(PlayerEntity player) {
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("suplex"), 1.0f, false, true, 3000);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        BiConsumer<PlayerEntity, LivingEntity> logic = (attacker, target) -> {
            if (attacker.getPassengerList().isEmpty() && !attacker.getWorld().isClient) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

                target.startRiding(serverPlayer, true);
                target.updatePassengerPosition(serverPlayer);
                serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
            }
        };

        tasks.put(1, (user, action) -> {
            CombatUtils.perEnemyLogic(action, logic);
        });

        tasks.put(6, (user, action) -> {
            //CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
            if (!user.getPassengerList().isEmpty()) {
                CameraUtils.playCameraShake(user);
                user.getPassengerList().getFirst().damage(user.getDamageSources().generic(), 10f);
                SoundUtils.playSound(user, SoundRegistry.EARTH_IMPACT);
            }

            if (user.getWorld().isClient) return;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.removeAllPassengers();
            serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
            serverPlayer.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, serverPlayer.getSteppingPos(), 750);
        });

        HeroUtils.getHeroStack(player).set(Grab.GRAB_START, player.getWorld().getTime());
        HeroUtils.getHeroStack(player).set(Grab.GRAB_END, player.getWorld().getTime() + 6);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        CombatUtils.slow(player, 20, 2);
        AttackUtils.startAttackTimer(player, 40);
    }

}
