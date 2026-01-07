package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.HeldAbility;
import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DashAbility extends HeldAbility {
    public DashAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, hide, requiredHoldTime, keybind);
    }

    // Todo: make it so that super leap doesn't trigger automatically when equipping new stack
    @Override
    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!AbilityUtils.canUseAbility(player, this.getAbilityID())) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, this.getKeybind());

        long heldFor = player.getWorld().getTime() - data.startTimestamp();

        if (data.held()) {
            Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
            long cooldownEnd = cooldownData.getOrDefault(this.getAbilityID(), 0L);

            if (heldFor > 3 && data.startTimestamp() >= cooldownEnd) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 1, false, false, false));
            }
        } else {
            KeybindingUtils.endKeybindHold(player, this.getKeybind());

            if (heldFor >= this.getRequiredHoldTime()) {
                Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
                long cooldownEnd = cooldownData.getOrDefault(this.getAbilityID(), 0L);

                if (data.startTimestamp() >= cooldownEnd) {
                    this.getAbilityLogic().accept(player);
                    AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown() * 2L);
                }
            } else {
                DashAbility.dash(player);
                AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown());
            }
        }
    }

    public static void dash(PlayerEntity player) {
        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ROLLING_END, player.getWorld().getTime() + 20);
        if (!player.getWorld().isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
        }
    }

    public static void chargedLeap(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        HeroUtils.getHeroStack(player).set(BlackSparksHero.CHARGED_LEAP_TIME_WINDOW, player.getWorld().getTime() + 15);

        if (player.isOnGround()) {
            SoundUtils.playSound(player, SoundRegistry.ROCK_CRUMBLING);
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
        } else {
            SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        }

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("front_handspring"), true, 5000);
        player.addVelocity(player.getRotationVector().multiply(2.5, 0, 2.5).add(0, 2, 0));
        player.velocityDirty = true;
        player.velocityModified = true;
        CameraUtils.playCameraShake(player);
    }
}
