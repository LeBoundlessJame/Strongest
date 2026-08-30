package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.mechanics.BlackFlashManager;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.BlackFlashable;

import java.util.List;

public class AttackResolver {
    public static Hit resolveHit(Hit hit) {
        // Todo: I gotta make it so that some of this stuff is pulled in from custom reusable resolvers
        if (hit.getAbility() instanceof BlackFlashable && BlackFlashManager.shouldBlackFlash(hit.getAttacker())) {
            hit.multiplyDamage(BlackFlashManager.getBlackFlashMultiplier(hit.getAttacker()));
            hit.setKnockback(hit.getKnockback().multiply(1.5f, 0.75f, 1.5f));
            hit.getHitEffects().addSounds(List.of(SoundRegistry.EARTH_IMPACT, SoundRegistry.ENERGY_IMPACT_2, SoundRegistry.ENERGY_IMPACT_3, SoundRegistry.ENERGY_IMPACT_HEAVY));
            hit.getHitEffects().addVisual(BoundlessAPI.id("black_flash_impact"));
        }

        return hit;
    }
}
