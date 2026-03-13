package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.combat.CombatSystem;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

public class MeleeAbilities {
    public static Ability DODGE = AbilityUtils.ability(MeleeAbilities::dash, 20, BoundlessAPI.identifier("dodge"), "Dodge");

    public static void dash(PlayerEntity player) {
        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ROLLING_END, player.getWorld().getTime() + 20);
        if (!player.getWorld().isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
        }

        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 5, 10);
        CombatSystem.moveToEntity(player, 32, 20, 2, 5.5f);
        EffekUtils.playVisual(player, BoundlessAPI.identifier("flight_boost"));
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("offensive_dash"));
    }

    public static void basicPerEnemyLogic(PlayerEntity player, Entity target, int slowDuration, int slowStr, int disorientDuration) {
        target.timeUntilRegen = 0;

        if (target instanceof LivingEntity livingEntity && !player.getWorld().isClient) {
            CombatUtils.slow(livingEntity, slowDuration, slowStr);

            livingEntity.setVelocity(player.getRotationVector().normalize().multiply(0.5, 0.25, 0.5).add(player.getVelocity()));
            livingEntity.velocityModified = true;
            livingEntity.velocityDirty = true;
        }

        if (target instanceof PlayerEntity playerTarget) {
            playerTarget.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.LIMITED_SPEED, ConfigRegistry.HERO_CONFIG.COMBAT_CONFIG.sprintSpeedLimitDuration.get(), 0, false, false, false));
            MeleeUtils.disorient(playerTarget, disorientDuration);
        }
    }

}
