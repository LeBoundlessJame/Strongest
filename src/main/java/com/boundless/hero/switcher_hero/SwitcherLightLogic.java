package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.SingleAttack;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;

public class SwitcherLightLogic {
    public static void lightAttack(PlayerEntity player) {
        if (TargetSelectMenu.isMenuOpen(player)) {
            TargetSelectMenu.selectTarget(player, "primary");
            return;
        }

        if (!AttackUtils.canAttack(player)) return;

        SingleAttack hook = SingleAttack.builder()
                .player(player)
                .damage(SwitcherHero.DAMAGE.lightAttack.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.id("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }
}
