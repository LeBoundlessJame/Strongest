package com.boundless.ability;

import net.minecraft.entity.player.PlayerEntity;

public abstract class TechniqueAbility {
    public abstract void activate(PlayerEntity playerEntity);

    public int getCost() {
        return 0;
    }

    public int getCooldown() {
        return 0;
    }

    public boolean canActivate() {
        return true;
    }
}
