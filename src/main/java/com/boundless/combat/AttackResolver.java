package com.boundless.combat;

import com.boundless.hero.api.HeroData;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class AttackResolver {

    public static AttackContext resolveAttack(PlayerEntity player) {
        // Todo: Might want to attack modifiers component based in the future or something
        // Todo: so that yuta can steal ratio attack modifier or MBA or something
        HeroData heroData = HeroUtils.getHeroData(player);
        List<AttackModifier> activeModifiers = new ArrayList<>();

        for (AttackModifier modifier: heroData.getAttackModifiers()) {
            if (modifier.shouldTrigger(player)) {
                activeModifiers.add(modifier);
            }
        }

        return new AttackContext(activeModifiers);
    }

    public static Hit resolveHit(Hit hit, AttackContext context) {
        for (AttackModifier modifier: context.getActiveModifiers()) {
            modifier.apply(hit);
        }
        return hit;
    }
}
