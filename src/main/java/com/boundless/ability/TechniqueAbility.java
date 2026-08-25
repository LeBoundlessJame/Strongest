package com.boundless.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class TechniqueAbility {
    public abstract void activate(PlayerEntity playerEntity);
    public abstract Identifier getAbilityId();
    public String getDisplayString() { return null; }

    public int getCost() {
        return 0;
    }

    public int getCooldown() {
        return 0;
    }

    public boolean canActivate(PlayerEntity playerEntity) {
        return true;
    }
}
