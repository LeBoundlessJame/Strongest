package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.util.AnimationUtils;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ShrineHeroOther {
    public static void heal(PlayerEntity player) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 2, true, false, false));
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("shrine_heal"));
        EffekUtils.playEffect(BoundlessAPI.identifier("rct_burst"), player, player.getPos().add(0, player.getHeight() / 2, 0), new Vec3d(0.2, 0.2, 0.2));
    }
}
