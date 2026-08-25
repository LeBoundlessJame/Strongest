package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class NonSneakAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity playerEntity) {
        System.out.println("Not sneaking");
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.identifier("non_sneak");
    }

    @Override
    public String getDisplayString() {
        return "Non sneaky ability";
    }
}
