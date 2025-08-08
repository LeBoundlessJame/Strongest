package com.boundless.ability.reusable_abilities.flight;

import com.boundless.BoundlessAPI;
import com.boundless.ability.components.KeybindHoldData;
import com.boundless.hero.SuperHero;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.networking.payloads.evasion.EvasionServerPayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class FlightAbility {
    public static void clientTick(MinecraftClient client) {
        if (client.player == null) return;
        KeybindingUtils.keybindHoldLogic(client, client.options.forwardKey, client.options.forwardKey.getTranslationKey());
    }

    // Todo: don't forget to fix melee animations etc playing on top, currently null takes priority over them
    // Todo: look into priority system, but also make sure that the animations stop getting triggered every tick
    // Todo: and instead use some sort of marking system
    public static void flightTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        FlightAbility.flightAnimationLogic(player);

        if (!player.getAbilities().flying) {
            DataComponentUtils.setInt(SuperHero.FLIGHT_TICKS, player, 0);
            return;
        }

        if (player.isSprinting()) {
            DataComponentUtils.addOrSubtractInt(SuperHero.FLIGHT_TICKS, player, 1, Integer.MAX_VALUE);
            if (DataComponentUtils.getInt(SuperHero.FLIGHT_TICKS, player, 0) == 1) {
                HeroUtils.getHeroStack(player).set(SuperHero.FLIGHT_BEGIN_TIMESTAMP, player.getWorld().getTime());
                boostLogic(player);
            }

            FlightAbility.flightMovement(player);
        } else {
            DataComponentUtils.setInt(SuperHero.FLIGHT_TICKS, player, 0);
        }
    }

    public static void flightAnimationLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!player.getAbilities().flying) return;

        if (player.isSprinting() && DataComponentUtils.getInt(SuperHero.FLIGHT_TICKS, player, 0) == 1) {
            AnimationUtils.playAnimation(player, BoundlessAPI.identifier("flight_pose"));
        } else if (!player.isSprinting()) {
            AnimationUtils.playAnimation(player, BoundlessAPI.identifier("hover"));
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
        SoundUtils.playSound(player, SoundEvents.ITEM_FIRECHARGE_USE, 1.0f);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 5, 0, true, false, false));

        Vec3d playerRotation = player.getRotationVector();
        Vec3d effectPos = player.getPos().add(playerRotation.normalize().multiply(-player.getWidth()).x, 0.5f, playerRotation.normalize().multiply(-player.getWidth() ).z);
        Vec3d effectScale = new Vec3d(player.getScale() * 0.5f, player.getScale() * 0.5f, player.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(BoundlessAPI.identifier("flight_boost"), player, effectPos, effectScale, effectRotation);
    }
}