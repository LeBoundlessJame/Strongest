package com.boundless.ability;

import com.boundless.mechanics.CooldownManager;
import com.boundless.mechanics.CursedEnergyManager;
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
    public int cost = 0;

    public final void use(PlayerEntity player) {
        if (!canActivate(player)) return;
        activate(player);

        if (this.getCost() > 0 && !player.isCreative()) CursedEnergyManager.addCursedEnergy(player, -this.getCost());
        CooldownManager.setAbilityCooldown(player, this.getAbilityId(), this.getCooldown());
    }

    public boolean canActivate(PlayerEntity player) {
        return !CooldownManager.isOnCooldown(player, this.getAbilityId()) && (this.getCost() == 0 || CursedEnergyManager.getCursedEnergy(player) >= this.getCost());
    }

    public long getCooldown(PlayerEntity player) {
        return this.getCooldown();
    }

    public long getCost(PlayerEntity player) {
        return this.getCost();
    }
}
