package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class HeadbuttLogic {

    // Todo: clean this up a LOT
    public static void headbutt(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;
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

        tasks.put(10, (user, action) -> {
            CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
        });

        tasks.put(20, (user, action) -> {
            CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
        });

        tasks.put(30, (user, action) -> {
            CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
        });

        tasks.put(40, (user, action) -> {
            CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
        });

        tasks.put(50, (user, action) -> {
            CombatUtils.attack(action, 10f, Optional.of(BoundlessAPI.identifier("melee_impact_crit")));
            if (user.getWorld().isClient) return;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.removeAllPassengers();
            serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
        });

        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("grab_and_punch"), 1.0f, false, true, 2000);
    }
}
