package com.boundless.combat.attack_modifiers;

import com.boundless.BoundlessAPI;
import com.boundless.combat.AttackModifier;
import com.boundless.combat.Hit;
import com.boundless.mechanics.BlackFlashManager;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.CameraUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class BlackFlashModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(LivingEntity user) {
        return BlackFlashManager.shouldBlackFlash(user);
    }

    @Override
    public void apply(Hit hit) {
        hit.multiplyDamage(BlackFlashManager.getBlackFlashMultiplier(hit.getAttacker()));
        hit.setKnockback(hit.getKnockback().multiply(3f, 1.5f, 3f));
        hit.getHitEffects().addSounds(List.of(SoundRegistry.EARTH_IMPACT, SoundRegistry.ENERGY_IMPACT_2, SoundRegistry.ENERGY_IMPACT_3, SoundRegistry.ENERGY_IMPACT_HEAVY));
        hit.getHitEffects().addVisual(BoundlessAPI.id("black_flash"));
    }

    @Override
    public void onTrigger(LivingEntity user) {
        int duration = 5;
        // TODO: FIX ASAP CameraUtils.playCameraShake(user);
        user.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, duration - 2, 4, true, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, duration, 4, true, false, false));
    }
}
