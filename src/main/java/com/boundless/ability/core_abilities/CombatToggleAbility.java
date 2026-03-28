package com.boundless.ability.core_abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import net.minecraft.entity.player.PlayerEntity;

public class CombatToggleAbility extends Ability {
    public CombatToggleAbility() {
        super(BoundlessAPI.identifier("combat_toggle"));
    }

    @Override
    public void executeAbility(PlayerEntity player) {

    }
}
