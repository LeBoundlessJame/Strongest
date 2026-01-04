package com.boundless.ability;

import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.action.Action;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

@Setter
@Getter
public class MeleeAbility {
    private AttackDataBuilder attackData;

    public MeleeAbility(AttackDataBuilder attackData) {
        this.attackData = attackData;
    }

    public void attack(PlayerEntity player) {
        int jabCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ATTACK_COUNT, jabCount + 1);

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, attackData.getSlownessDuration(), attackData.getSlownessAmplifier(), true, false, false));
        Action attackAction = ActionUtils.singleAction(attackData.getImpactTick(), (attacker, heroAction) -> {
            CombatUtils.attack(heroAction, attackData);
        });

        AnimationUtils.playSyncedAnimation(player, attackData.getAnimation(), attackData.getAnimationSpeed(), jabCount % 2 == 0, true, 2000);
        AttackUtils.triggerAttackAction(player, attackAction);
    }
}
