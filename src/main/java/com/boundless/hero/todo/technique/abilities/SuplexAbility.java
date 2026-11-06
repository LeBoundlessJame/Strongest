package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.tick.TickScheduler;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

import java.util.List;
import java.util.function.BiConsumer;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

public class SuplexAbility extends TechniqueAbility {

    @Override
    public void activate(PlayerEntity player) {
        if (!(player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L))) return;

        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("suplex"), 1.0f, false, true, 3000);

        List<LivingEntity> targets = new MeleeHitbox(5.0, 180.0).getTargetsInArc(player);

        if (!targets.isEmpty()) {
            TickScheduler.schedule(player.getWorld(), 5, () -> this.startSuplex(player, targets.getFirst()));
            TickScheduler.schedule(player.getWorld(), 6, () -> this.endSuplex(player));
        }

        AttackUtils.startAttackTimer(player, 40);
    }

    public void endSuplex(PlayerEntity player) {
        if (!player.getPassengerList().isEmpty()) {
            CameraUtils.playCameraShake(player);
            player.getPassengerList().getFirst().damage(player.getDamageSources().generic(), 10f);
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
        }

        if (player.getWorld().isClient) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        serverPlayer.removeAllPassengers();
        serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
        serverPlayer.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, serverPlayer.getSteppingPos(), 750);
    }

    public void startSuplex(PlayerEntity player, LivingEntity target) {
        if (target == null) return;

        if (player.getPassengerList().isEmpty() && !player.getWorld().isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            target.startRiding(serverPlayer, true);
            target.updatePassengerPosition(serverPlayer);
            serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
        }
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("suplex");
    }

    public float getDamage(PlayerEntity player) {
        return 100;
    }
}
