package com.boundless.ability.reusable_abilities.flight;

import com.boundless.BoundlessAPI;
import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FlightAbility {
    public static ComponentType<Integer> FLIGHT_TICKS = DataComponentRegistry.registerInt("flight_ticks");
    public static ComponentType<Long> FLIGHT_BEGIN_TIMESTAMP = DataComponentRegistry.registerLong("flight_begin_timestamp");
    public static ComponentType<Boolean> BOOSTING = DataComponentRegistry.registerBoolean("boosting");
    public static ComponentType<Integer> BOOST_TICKS = DataComponentRegistry.registerInt("boost_ticks");
    public static ComponentType<Long> BOOST_NEXT_USABLE = DataComponentRegistry.registerLong("boost_next_usable");
    public static ComponentType<Float> FLIGHT_ROTATION = DataComponentRegistry.registerFloat("flight_rotation");

    // Todo: don't forget to fix melee animations etc playing on top, currently null takes priority over them
    // Todo: look into priority system, but also make sure that the animations stop getting triggered every tick
    // Todo: and instead use some sort of marking system
    public static void flightTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        FlightAbility.flightAnimationLogic(player);
        FlightAbility.rotationLogic(player);

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

    public static void rotationLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        KeybindHoldData forwardData = KeybindingUtils.getHoldData(player, "key.forward");
        KeybindHoldData backData = KeybindingUtils.getHoldData(player, "key.back");

        float rotation = heroStack.getOrDefault(FlightAbility.FLIGHT_ROTATION, 0f);
        int duration = player.isSprinting() && rotation > 0 ? 15 : 10;
        float clamp = player.isSprinting() ? 1.0f : 0.2f;
        float rotationSpeed = clamp / duration;

        if (forwardData.held()) {
            rotation = MathHelper.clamp(rotation + rotationSpeed, 0f, clamp);
        } else if (rotation > 0) {
            rotation = MathHelper.clamp(rotation - rotationSpeed, 0f, clamp);
        } else if (backData.held()) {
            rotation = MathHelper.clamp(rotation - rotationSpeed, -clamp, 0f);
        } else if (rotation < 0) {
            rotation = MathHelper.clamp(rotation + rotationSpeed, -clamp, 0f);
        }

        heroStack.set(FlightAbility.FLIGHT_ROTATION, rotation);
    }


    public static void flightAnimationLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!player.getAbilities().flying) return;

        if (player.isSprinting() && DataComponentUtils.getInt(FLIGHT_TICKS, player, 0) == 1) {
            AnimationUtils.playAnimation(player, BoundlessAPI.identifier("flight_pose"), false);
        } else if (!player.isSprinting()) {
            AnimationUtils.playAnimation(player, BoundlessAPI.identifier("hover"), false);
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