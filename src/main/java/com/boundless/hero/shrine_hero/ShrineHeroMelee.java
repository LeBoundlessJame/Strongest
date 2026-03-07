package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.MeleeAbilities;
import com.boundless.action.SingleAttack;
import com.boundless.combat.Combo;
import com.boundless.hero.switcher_hero.SwitcherHero;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ShrineHeroMelee {
    public static Ability LIGHT_ATTACK = AbilityUtils.ability(ShrineHeroMelee::lightAttack, ShrineHero.COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("shrine_light_attack"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(ShrineHeroMelee::mediumAttack, ShrineHero.COOLDOWNS.mediumAttack.get(), BoundlessAPI.identifier("shrine_medium_attack"), BoundlessAPI.hudPNG("leg"));

    public static void mediumAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player) || player.isUsingItem()) return;
        boolean comboTriggered = false;

        for (Combo combo: ShrineHero.COMBOS) {
            if (!comboTriggered && combo.matchesTargetCombo(player, "m")) comboTriggered = true;
            combo.updateAndEvaluateCombo(player, "m");
        }

        if (comboTriggered) return;

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
        boolean comboTriggered = false;

        for (Combo combo: ShrineHero.COMBOS) {
            if (!comboTriggered && combo.matchesTargetCombo(player, "l")) comboTriggered = true;
            combo.updateAndEvaluateCombo(player, "l");
        }

        if (comboTriggered) return;

        SingleAttack hook = SingleAttack.builder()
                .player(player)
                .damage(ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestLightAttack.get(), ShrineHero.DAMAGE.strongestLightAttack.get()))
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .animationSpeed(1.0f)
                .animation(BoundlessAPI.identifier("hook"))
                .impactTick(4)
                .attackDuration(4)
                .perEntityLogic((user, target) ->
                MeleeAbilities.basicPerEnemyLogic(user, target, 14, 255, 8))
                .build();

        MeleeUtils.disorient(player, 5);
        AttackUtils.performAttack(hook);
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.LIMITED_SPEED, ConfigRegistry.HERO_CONFIG.COMBAT_CONFIG.sprintSpeedLimitDuration.get(), 0, false, false, false));
    }

    public static void knockbackAttack(PlayerEntity player) {

        /*
        CameraUtils.playCameraShake(player);
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 4, 5, false, false, false));
        MeleeUtils.knockback(player, livingEntity, new Vec3d(2.5f, 0.4f, 2.5f));

         */

        //player.getWorld().setBlockBreakingInfo(player.getId(), player.getBlockPos().down(), 5);
    }
}
