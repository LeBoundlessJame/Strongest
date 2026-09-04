package com.boundless.util;

import com.boundless.ability.generic.AOEAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface AOE {
    default void activateAOE(PlayerEntity player) {
        AOEAbility aoeAbility = (AOEAbility) this;
        List<LivingEntity> targets = AOEUtils.getTargetsInRadius(player, player.getWorld(), player.getPos(), aoeAbility.getRadius(), entity -> true);
        CombatUtils.applyHits(player, targets, aoeAbility.getDamage(), aoeAbility.getKnockback(), aoeAbility.getOnHit(), aoeAbility.getHitEffects());
    }
}
