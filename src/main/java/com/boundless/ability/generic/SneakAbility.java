package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class SneakAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity playerEntity) {
        System.out.println("Sneaky boi");
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.identifier("sneak");
    }
}
