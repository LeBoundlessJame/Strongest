package com.boundless.combat;

import com.boundless.mechanics.BlackFlashManager;
import com.boundless.util.BlackFlashable;

public class AttackResolver {
    public static Hit resolveHit(Hit hit) {
        // Todo: I gotta make it so that some of this stuff is pulled in from custom reusable resolvers
        if (hit.getAbility() instanceof BlackFlashable && BlackFlashManager.shouldBlackFlash(hit.getAttacker())) {
            hit.multiplyDamage(BlackFlashManager.getBlackFlashMultiplier(hit.getAttacker()));
            hit.setKnockback(hit.getKnockback().multiply(1.5f, 0.75f, 1.5f));
            System.out.println("Kokusen!");
        }

        return hit;
    }
}
