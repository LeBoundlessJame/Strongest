package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

// Todo: might not need this in the future, but I'll use it for now
public class EmptyAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity playerEntity) {}

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.identifier("empty");
    }
}
