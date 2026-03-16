package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.util.AnimationUtils;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.MeleeUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class BlockLogic {
    public static void tick(PlayerEntity player) {
        blockAnimation(player);

        if (!isBlocking(player)) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, 1, false, false));
        MeleeUtils.disorient(player, 2);
        player.setSprinting(false);
    }

    public static void blockAnimation(PlayerEntity player) {
        if (AnimationUtils.getLastServerTriggeredAnimation(player).equals(BoundlessAPI.identifier("block")) && !isBlocking(player)) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("idle"), 1.0f, false, false, 9999);
        } else if (isBlocking(player) && KeybindingUtils.getHeldTime(player, "key.use") == 1L) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("block"), 2.0f, false, true, 9998);
        }
    }

    public static boolean isBlocking(PlayerEntity player) {
        return KeybindingUtils.isHoldingKey(player, "key.use");
    }
}
