package com.boundless.combat.attack_modifiers;

import com.boundless.BoundlessAPI;
import com.boundless.combat.AttackModifier;
import com.boundless.combat.Hit;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.ComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class DivergentModifier implements AttackModifier {
    @Override
    public boolean shouldTrigger(PlayerEntity player) {
        //ComponentUtils.get(player, YujiComponents.DIVERGENCE_ACTIVE) != null
        return true;
    }

    @Override
    public void apply(Hit hit) {

    }

    @Override
    public void onTrigger(PlayerEntity player) {
        //TickScheduler.schedule(player.getWorld(), 20, () -> System.out.println("IT WORKS!"));
    }

    @Override
    public void postTrigger(PlayerEntity player) {
        //ComponentUtils.remove(player, YujiComponents.DIVERGENCE_ACTIVE);
    }
}
