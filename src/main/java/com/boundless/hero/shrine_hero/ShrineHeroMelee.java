package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.SingleAttack;
import com.boundless.hero.switcher_hero.SwitcherHero;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.player.PlayerEntity;

import static com.boundless.hero.black_sparks_hero.BrawlerHero.COOLDOWNS;

public class ShrineHeroMelee {
    public static Ability LIGHT_ATTACK = AbilityUtils.ability(ShrineHeroMelee::lightAttack, ShrineHero.COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("shrine_light_attack"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(ShrineHeroMelee::mediumAttack, ShrineHero.COOLDOWNS.mediumAttack.get(), BoundlessAPI.identifier("shrine_medium_attack"), BoundlessAPI.hudPNG("leg"));

    public static void mediumAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player) || player.isUsingItem()) return;

        SingleAttack doubleKick = SingleAttack.builder()
                .player(player)
                .damage(SwitcherHero.DAMAGE.mediumAttackPerHit.get())
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .damage(ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestMediumAttackPerHit.get(), ShrineHero.DAMAGE.strongestMediumAttackPerHit.get()))
                .animation(BoundlessAPI.identifier("double_kick"))
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
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .build();

        AttackUtils.performAttack(hook);
    }
}
