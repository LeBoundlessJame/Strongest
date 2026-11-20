package com.boundless.util;

import com.boundless.ability.generic.AOEAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface AOE {
    default void activateAOE(LivingEntity user) {
        AOEAbility aoeAbility = (AOEAbility) this;
        List<LivingEntity> targets = AOEUtils.getTargetsInRadius(user, user.getWorld(), user.getPos(), aoeAbility.getRadius(), entity -> true);
        CombatUtils.applyHits(user, targets, aoeAbility.getDamage(), aoeAbility.getKnockback(), aoeAbility.getOnHit(), aoeAbility.getHitEffects());
    }
}
