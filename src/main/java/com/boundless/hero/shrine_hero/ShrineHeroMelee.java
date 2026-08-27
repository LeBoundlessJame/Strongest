package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.SingleAttack;
import com.boundless.hero.switcher_hero.SwitcherHero;
import com.boundless.registry.SoundRegistry;
import com.boundless.mechanics.AbilityManager;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;

public class ShrineHeroMelee {
    public static Ability LIGHT_ATTACK = AbilityManager.ability(ShrineHeroMelee::lightAttack, ShrineHero.COOLDOWNS.lightAttack.get(), BoundlessAPI.id("shrine_light_attack"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityManager.ability(ShrineHeroMelee::mediumAttack, ShrineHero.COOLDOWNS.mediumAttack.get(), BoundlessAPI.id("shrine_medium_attack"), BoundlessAPI.hudPNG("leg"));

    public static void mediumAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player) || player.isUsingItem()) return;

        SingleAttack doubleKick = SingleAttack.builder()
                .player(player)
                .damage(SwitcherHero.DAMAGE.mediumAttackPerHit.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .damage(ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestMediumAttackPerHit.get(), ShrineHero.DAMAGE.strongestMediumAttackPerHit.get()))
                .animation(BoundlessAPI.id("double_kick"))
                .impactTick(4)
                .attackDuration(8)
                .build();

        AttackUtils.performAttack(doubleKick);
    }

    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        SingleAttack hook = SingleAttack.builder()
                .player(player)
                .damage(ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestLightAttack.get(), ShrineHero.DAMAGE.strongestLightAttack.get()))
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.id("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }
}
