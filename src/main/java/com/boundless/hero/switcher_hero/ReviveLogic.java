package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.AnimationUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ReviveLogic {
    public static void revive(PlayerEntity player) {
        EffekUtils.playEffect(BoundlessAPI.identifier("todo_aura"), player, player.getPos(), new Vec3d(3, 3, 3));
        EffekUtils.playEffect(BoundlessAPI.identifier("healing_burst"), player, player.getPos(), new Vec3d(1, 1, 1));
        HeroUtils.getHeroStack(player).set(SwitcherHero.REVIVE_TIME, player.getWorld().getTime() + 10);

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("revive"));

        player.setHealth(1.0F);
        player.clearStatusEffects();
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 255, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 100, 0, false, false, true));
    }
}
