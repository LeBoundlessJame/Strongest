package com.boundless.combat;

import com.boundless.mechanics.BlackFlashManager;
import com.boundless.util.BlackFlashable;

public class AttackResolver {
    public static Hit resolveHit(Hit hit) {
        if (hit.getAbility() instanceof BlackFlashable && BlackFlashManager.shouldBlackFlash(hit.getAttacker())) {
            hit.multiplyDamage(BlackFlashManager.getBlackFlashMultiplier(hit.getAttacker()));
        }

        return hit;
    }
}
