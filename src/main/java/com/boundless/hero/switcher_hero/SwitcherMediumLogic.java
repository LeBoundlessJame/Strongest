package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.SingleAttack;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;

public class SwitcherMediumLogic {
    public static void mediumAttack(PlayerEntity player) {
        if (TargetSelectMenu.isMenuOpen(player)) {
            TargetSelectMenu.selectTarget(player, "secondary");
            return;
        }

        if (!AttackUtils.canAttack(player)) return;

        SingleAttack doubleKick = SingleAttack.builder()
                .player(player)
                .damage(SwitcherHero.DAMAGE.mediumAttackPerHit.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .damage(12f)
                .animation(BoundlessAPI.id("double_kick"))
                .impactTick(4)
                .attackDuration(8)
                .build();

        AttackUtils.performAttack(doubleKick);

        /*
        CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                target.addVelocity(0, 0.5f, 0);
                target.velocityModified = true;
                target.timeUntilRegen = 0;
            });
         */


        /*
        tasks.put(4, kick);
        tasks.put(8, kick);

         */
    }
}
