package com.boundless.combat;

import com.boundless.hero.api.HeroData;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class AttackResolver {

    public static AttackContext resolveAttack(LivingEntity user) {
        // Todo: Might want to attack modifiers component based in the future or something
        // Todo: so that yuta can steal ratio attack modifier or MBA or something
        HeroData heroData = HeroUtils.getHeroData(user);
        List<AttackModifier> activeModifiers = new ArrayList<>();

        for (AttackModifier modifier: heroData.getAttackModifiers()) {
            if (modifier.shouldTrigger(user)) {
                activeModifiers.add(modifier);
                modifier.onTrigger(user);
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
