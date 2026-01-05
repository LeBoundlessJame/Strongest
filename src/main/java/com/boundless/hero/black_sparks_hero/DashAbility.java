package com.boundless.hero.black_sparks_hero;

import com.boundless.ability.HeldAbility;
import com.boundless.ability.components.KeybindHoldData;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.CameraUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DashAbility extends HeldAbility {
    public DashAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, hide, requiredHoldTime, keybind);
    }

    @Override
    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!AbilityUtils.canUseAbility(player, this.getAbilityID())) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, this.getKeybind());

        long heldFor = player.getWorld().getTime() - data.startTimestamp();

        if (data.held()) {
            Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
            long cooldownEnd = cooldownData.get(this.getAbilityID());

            if (heldFor > 3 && data.startTimestamp() >= cooldownEnd) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 1, false, false, false));
            }
        } else {
            KeybindingUtils.endKeybindHold(player, this.getKeybind());

            if (heldFor >= this.getRequiredHoldTime()) {
                Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
                long cooldownEnd = cooldownData.get(this.getAbilityID());

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
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ROLLING_END, player.getWorld().getTime() + 20);
        if (!player.getWorld().isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
        }
    }

    // Todo: Camera shake suddenly stopped working. Try to fix later.
    public static void superDash(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        player.addVelocity(player.getRotationVector().multiply(5));
        player.velocityDirty = true;
        player.velocityModified = true;
        CameraUtils.playCameraShake(player);
    }
}
