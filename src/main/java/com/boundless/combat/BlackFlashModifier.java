package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.mechanics.BlackFlashManager;
import com.boundless.registry.SoundRegistry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class BlackFlashModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(PlayerEntity player) {
        return BlackFlashManager.shouldBlackFlash(player);
    }

    @Override
    public void apply(Hit hit) {
        hit.multiplyDamage(BlackFlashManager.getBlackFlashMultiplier(hit.getAttacker()));
        hit.setKnockback(hit.getKnockback().multiply(3f, 1.5f, 3f));
        hit.getHitEffects().addSounds(List.of(SoundRegistry.EARTH_IMPACT, SoundRegistry.ENERGY_IMPACT_2, SoundRegistry.ENERGY_IMPACT_3, SoundRegistry.ENERGY_IMPACT_HEAVY));
        hit.getHitEffects().addVisual(BoundlessAPI.id("black_flash"));
    }
}
