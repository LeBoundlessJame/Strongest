package com.boundless.combat;

import com.boundless.ability.Ability;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class MeleeAbility extends Ability {
    public MeleeAbility(Identifier id) {
        super(id);
    }

    @Override
    public void executeAbility(PlayerEntity player) {

    }

    @Override
    public boolean canUseAbility(PlayerEntity player) {
        boolean canUse = super.canUseAbility(player);
        canUse &= HeroUtils.combatModeEnabled(player);
        return canUse;
    }
}
