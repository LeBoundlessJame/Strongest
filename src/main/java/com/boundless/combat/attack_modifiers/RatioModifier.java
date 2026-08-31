package com.boundless.combat.attack_modifiers;

import com.boundless.BoundlessAPI;
import com.boundless.combat.AttackModifier;
import com.boundless.combat.Hit;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.mechanics.BlackFlashManager;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class RatioModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(RatioComponents.RATIO_NEXT_ATTACK, false);
    }

    @Override
    public void apply(Hit hit) {
        hit.multiplyDamage(1.75f);
        hit.addDamage(hit.getTarget().getMaxHealth() * 0.07f);
        hit.getHitEffects().addSounds(List.of(SoundRegistry.ENERGY_IMPACT_2, SoundRegistry.ENERGY_IMPACT_HEAVY));
        hit.getHitEffects().addVisual(BoundlessAPI.id("ratio"));
    }

    @Override
    public void onTrigger(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(RatioComponents.RATIO_NEXT_ATTACK, false);
    }
}
