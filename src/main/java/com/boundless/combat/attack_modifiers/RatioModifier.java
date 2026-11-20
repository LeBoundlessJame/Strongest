package com.boundless.combat.attack_modifiers;

import com.boundless.BoundlessAPI;
import com.boundless.combat.AttackModifier;
import com.boundless.combat.Hit;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class RatioModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(LivingEntity user) {
        return HeroUtils.getHeroStack(user).get(RatioComponents.NEXT_ATTACK_RATIO_MULTIPLIER) != null;
    }

    @Override
    public void apply(Hit hit) {
        float ratioMultiplier = HeroUtils.getHeroStack(hit.getAttacker()).get(RatioComponents.NEXT_ATTACK_RATIO_MULTIPLIER);
        hit.multiplyDamage(ratioMultiplier);
        hit.addDamage(hit.getTarget().getMaxHealth() * 0.07f);
        hit.getHitEffects().addVisual(BoundlessAPI.id("ratio_impact"));
        hit.getHitEffects().addSounds(List.of(SoundRegistry.RATIO_IMPACT_1, SoundRegistry.RATIO_IMPACT_2));
    }

    @Override
    public void onTrigger(LivingEntity user) {
        user.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 5, 4, true, false, false));
    }

    @Override
    public void postTrigger(LivingEntity user) {
        HeroUtils.getHeroStack(user).remove(RatioComponents.NEXT_ATTACK_RATIO_MULTIPLIER);
    }
}
