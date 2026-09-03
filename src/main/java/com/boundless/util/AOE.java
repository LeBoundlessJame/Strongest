package com.boundless.util;

import com.boundless.ability.generic.AOEAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public interface AOE {
    default void activateAOE(PlayerEntity player, HeroActionEntity action) {
        AOEAbility aoeAbility = (AOEAbility) this;
        action.repositionBox();
        List<LivingEntity> targets = AOEUtils.getTargetsInRadius(player, player.getWorld(), player.getPos(), aoeAbility.getRadius(), entity -> true);
        CombatUtils.applyHits(player, action, targets, aoeAbility.getDamage(), aoeAbility.getKnockback(), aoeAbility.getOnHit(), aoeAbility.getHitEffects());
    }
}
