package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.SoundUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MeleeAbilities {
    public static Ability DODGE = AbilityUtils.ability(MeleeAbilities::dash, 20, BoundlessAPI.identifier("dodge"), "Dodge");

    public static void dash(PlayerEntity player) {
        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ROLLING_END, player.getWorld().getTime() + 20);
        if (!player.getWorld().isClient) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
        }
    }
}
