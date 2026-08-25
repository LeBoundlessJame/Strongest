package com.boundless.ability;

import com.boundless.mechanics.CooldownManager;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class TechniqueAbility {
    public abstract void activate(PlayerEntity player);
    public abstract Identifier getAbilityId();
    public String getDisplayString() { return null; }

    @Getter
    public long cooldown;
    @Getter
    public int cost;

    public final void use(PlayerEntity player) {
        if (!canActivate(player)) return;
        activate(player);
        CooldownManager.setAbilityCooldown(player, this.getAbilityId(), this.getCooldown());
    }

    public boolean canActivate(PlayerEntity player) {
        return !CooldownManager.isOnCooldown(player, this.getAbilityId());
    }
}
