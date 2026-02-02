package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Attack;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AttackUtils;
import com.boundless.util.CombatUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class SwitcherLightLogic {
    public static void lightAttack(PlayerEntity player) {
        if (TargetSelectMenu.isMenuOpen(player)) {
            TargetSelectMenu.selectTarget(player, "primary");
            return;
        }

        if (!AttackUtils.canAttack(player)) return;

        Attack hook = Attack.builder()
                .player(player)
                .damage(SwitcherHero.DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }
}
