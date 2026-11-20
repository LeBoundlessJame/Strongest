package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.tick.TickScheduler;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldEvents;

import java.util.List;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

public class SuplexAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (!(user.getWorld().getTime() >= HeroUtils.getHeroStack(user).getOrDefault(ATTACK_END, 0L))) return;

        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("suplex"), 1.0f, false, true, 3000);

        List<LivingEntity> targets = new MeleeHitbox(5.0, 180.0).getTargetsInArc(user);

        if (!targets.isEmpty()) {
            TickScheduler.schedule(user.getWorld(), 3, () -> this.startSuplex(user, targets.getFirst()));
            TickScheduler.schedule(user.getWorld(), 6, () -> this.endSuplex(user));
        }

        AttackUtils.startAttackTimer(user, 20);
    }

    public void endSuplex(LivingEntity user) {
        if (!user.getPassengerList().isEmpty()) {
            // TODO: FIX CameraUtils.playCameraShake(user);
            user.getPassengerList().getFirst().damage(user.getDamageSources().generic(), this.getDamage(user));
            SoundUtils.playSound(user, SoundRegistry.EARTH_IMPACT);
        }

        if (user.getWorld().isClient) return;

        // TODO: would be nice if this could be usable by non-players
        if (user instanceof PlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;
            serverPlayer.removeAllPassengers();
            serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
            serverPlayer.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, serverPlayer.getSteppingPos(), 750);
        }
    }

    public void startSuplex(LivingEntity user, LivingEntity target) {
        if (target == null) return;
        if (!user.getPassengerList().isEmpty() || user.getWorld().isClient) return;

        if (user instanceof PlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;

            target.startRiding(serverPlayer, true);
            target.updatePassengerPosition(serverPlayer);
            serverPlayer.networkHandler.send(new EntityPassengersSetS2CPacket(serverPlayer), null);
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("suplex");
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Suplex");
    }

    public float getDamage(LivingEntity user) {
        return ComponentUtils.getOr(user, StrongestComponents.MELEE_STRENGTH, 0f) * 4.5f;
    }
}
