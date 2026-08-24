package com.boundless.hero.shadow_hero.technique;

import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;

public class PunchAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity playerEntity) {
        System.out.println("Punch!");
    }
}
