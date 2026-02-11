package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Attack;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AnimationUtils;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;

public class BrawlerMelee {
    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        Attack hook = Attack.builder()
                .player(player)
                .damage(BrawlerHero.DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }

    public static void manjiKick(PlayerEntity player) {
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("manji_kick_parry"), 1f, false, true, 3000);
    }
}
