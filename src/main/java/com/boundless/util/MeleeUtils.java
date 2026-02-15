package com.boundless.util;

import com.boundless.entity.hero_action.HeroActionEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiConsumer;

public class MeleeUtils {
    public static void basicHit(PlayerEntity player) {

    }

    public void forEach(PlayerEntity player, HeroActionEntity action, BiConsumer<PlayerEntity, Entity> logic) {
        for (LivingEntity target : action.getWorld().getEntitiesByClass(LivingEntity.class, action.getBoundingBox(), entity -> true)) {
            if (target != player) logic.accept(player, target);
        }

        /*
                   //impactVisual.ifPresent((identifier) -> playImpactVisual(player, target, impactVisual.get()));
                //target.damage(target.getDamageSources().generic(), damage);
                //CombatUtils.uppercutKnockback(player, target);
         */
    }
}
