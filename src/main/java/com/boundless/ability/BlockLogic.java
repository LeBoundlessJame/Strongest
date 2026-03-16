package com.boundless.ability;

import com.boundless.util.KeybindingUtils;
import com.boundless.util.MeleeUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class BlockLogic {
    public static void tick(PlayerEntity player) {
        if (!isBlocking(player)) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 1, false, false));
        MeleeUtils.disorient(player, 5);
    }

    public static boolean isBlocking(PlayerEntity player) {
        return KeybindingUtils.isHoldingKey(player, "key.use");
    }
}
