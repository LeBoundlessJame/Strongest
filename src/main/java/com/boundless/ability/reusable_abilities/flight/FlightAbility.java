package com.boundless.ability.reusable_abilities.flight;

import com.boundless.BoundlessAPI;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class FlightAbility {
    public static ComponentType<Integer> FLIGHT_TICKS = DataComponentRegistry.registerInt("flight_ticks");
    public static ComponentType<Long> FLIGHT_BEGIN_TIMESTAMP = DataComponentRegistry.registerLong("flight_begin_timestamp");
    public static ComponentType<Integer> BOOST_TICKS = DataComponentRegistry.registerInt("boost_ticks");
    public static ComponentType<Boolean> FLYING = DataComponentRegistry.registerBoolean("flying");

    public static HashMap<Identifier, Integer> FLIGHT_ANIMATIONS = getFlightAnimations();

    public static HashMap<Identifier, Integer> getFlightAnimations() {
        HashMap<Identifier, Integer> animations = new HashMap<>();
        animations.put(BoundlessAPI.identifier("flight_pose"), 1100);
        animations.put(BoundlessAPI.identifier("hover"), 1200);
        return animations;
    }

    public static void tick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        FlightAbility.animationLogic(player);

        if (!player.getAbilities().flying) {
            DataComponentUtils.setInt(FLIGHT_TICKS, player, 0);
            return;
        }

        if (player.isSprinting()) {
            DataComponentUtils.addOrSubtractInt(FLIGHT_TICKS, player, 1, Integer.MAX_VALUE);
            if (DataComponentUtils.getInt(FLIGHT_TICKS, player, 0) == 1) {
                HeroUtils.getHeroStack(player).set(FLIGHT_BEGIN_TIMESTAMP, player.getWorld().getTime());
                boostLogic(player);
            }

            FlightAbility.flightMovement(player);
        } else {
            DataComponentUtils.setInt(FLIGHT_TICKS, player, 0);
            DataComponentUtils.addOrSubtractInt(BOOST_TICKS, player, 1, 100000);
        }
    }

    public static void animationLogic(PlayerEntity player) {
        if (player.getAbilities().flying) {
            HeroUtils.getHeroStack(player).set(FlightAbility.FLYING, true);

            if (player.isSprinting()) {
                AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("flight_pose"), false, 1100);
            } else {
                AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hover"), false, 1100);
            }
        } else if (player.age % 2 == 0) {
            HeroUtils.getHeroStack(player).set(FlightAbility.FLYING, false);
            AnimationUtils.stopSyncedAnimationIfPresent(player, FLIGHT_ANIMATIONS);
        }
    }

    public static void flightMovement(PlayerEntity player) {
        Vec3d rotation = player.getRotationVector().multiply(2f);
        player.setVelocity(rotation.x, rotation.y, rotation.z);
        player.velocityModified = true;
        player.velocityDirty = true;
        player.onLanding();
    }

    public static void boostLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        SoundUtils.playSound(player, SoundEvents.ITEM_FIRECHARGE_USE, 1.0f);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 0, true, false, false));

        ServerPlayNetworking.send((ServerPlayerEntity) player, new CameraShakePayload());
        Vec3d playerRotation = player.getRotationVector();
        Vec3d effectPos = player.getPos().add(playerRotation.normalize().multiply(-player.getWidth()).x, 0.5f, playerRotation.normalize().multiply(-player.getWidth() ).z);
        Vec3d effectScale = new Vec3d(player.getScale() * 0.5f, player.getScale() * 0.5f, player.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(BoundlessAPI.identifier("flight_boost"), player, effectPos, effectScale, effectRotation);
    }

    public static void initialize() {}
}