package com.boundless.ability.reusable_abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MeleeCombatAbilities {
    public static Ability JAB = Ability.builder()
            .abilityLogic((player) -> {
                new MeleeAbility(AttackDataBuilder.builder()
                        .damage(10f)
                        .knockbackStrength(1.0f)
                        .animation(BoundlessAPI.identifier("hook"))
                        .impactTick(4)
                        .attacker(player)
                        .build()).attack(player);
            })
            .cooldown(5)
            .abilityID(BoundlessAPI.identifier("jab"))
            .abilityIcon(BoundlessAPI.hudPNG("arm"))
            .build();

    public static Ability SPIN_KICK = Ability.builder()
            .abilityLogic((player) -> {
                new MeleeAbility(AttackDataBuilder.builder()
                        .damage(15f)
                        .knockbackStrength(2.0f)
                        .animation(BoundlessAPI.identifier("spin_kick"))
                        .impactTick(4)
                        .slownessDuration(11)
                        .slownessAmplifier(1)
                        .impactTick(10)
                        .animationSpeed(1.0f)
                        .attacker(player)
                        .build()).attack(player);
            })
            .cooldown(10)
            .abilityID(BoundlessAPI.identifier("spin_kick"))
            .abilityIcon(BoundlessAPI.hudPNG("leg"))
            .build();

    public static Ability DROPKICK = Ability.builder()
            .abilityLogic((player) -> {
                new MeleeAbility(AttackDataBuilder.builder()
                        .damage(30f)
                        .knockbackStrength(3.0f)
                        .animation(BoundlessAPI.identifier("dropkick_start"))
                        .impactTick(4)
                        .slownessDuration(20)
                        .slownessAmplifier(2)
                        .impactTick(4)
                        .animationSpeed(1.0f)
                        .attacker(player)
                        .build()).attack(player);
            })
            .cooldown(10)
            .abilityID(BoundlessAPI.identifier("dropkick"))
            .abilityIcon(BoundlessAPI.hudPNG("leg"))
            .build();

    public static Ability DODGE = Ability.builder()
            .abilityID(BoundlessAPI.identifier("dash"))
            .abilityIcon(BoundlessAPI.hudPNG("dash"))
            .cooldown(60)
            .abilityLogic((player) -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
                if (!player.getWorld().isClient) {
                    ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
                }
            })
            .build();

    public static Ability UPPERCUT = Ability.builder()
            .abilityLogic((player) -> {
                new MeleeAbility(AttackDataBuilder.builder()
                        .damage(50f)
                        .postHitLogic(CombatUtils::uppercutKnockback)
                        .replacedAttackLogic(MeleeCombatAbilities::uppercutMovement)
                        .knockbackStrength(1.0f)
                        .animation(BoundlessAPI.identifier("uppercut"))
                        .animationSpeed(2.0f)
                        .impactTick(4)
                        .slownessDuration(10)
                        .slownessAmplifier(0)
                        .impactTick(2)
                        .attacker(player)
                        .build()).attack(player);
            })
            .cooldown(60)
            .abilityID(BoundlessAPI.identifier("uppercut"))
            .abilityIcon(BoundlessAPI.hudPNG("sword"))
            .build();

    public static void uppercutMovement(AttackDataBuilder attack, PlayerEntity player) {
        float knockbackMultiplier = attack.getKnockbackStrength();

        player.setVelocity(player.getRotationVector().x * (1.2 * knockbackMultiplier), 1, player.getRotationVector().z * (1.2 * knockbackMultiplier));
        player.velocityModified = true;
    }
}