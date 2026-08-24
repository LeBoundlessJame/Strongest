package com.boundless.hero.shadow_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class PunchAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity playerEntity) {
        System.out.println("Punch!");
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.identifier("megumi_punch");
    }
}
