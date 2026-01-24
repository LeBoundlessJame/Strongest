package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Attack;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AttackUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class LightAttackLogic {
    public static void lightAttack(PlayerEntity player) {
        if (BoogieLogic.isSelectingBoogie(player)) {
            HeroUtils.getHeroStack(player).set(SwitcherHero.BOOGIE_SELECTION, "swapWithPrimary");
            return;
        }

        Attack hook = Attack.builder()
                .player(player)
                .damage(BlackSparksHero.DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }
}
