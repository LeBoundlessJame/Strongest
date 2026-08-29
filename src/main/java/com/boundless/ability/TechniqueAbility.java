package com.boundless.ability;

import com.boundless.mechanics.CooldownManager;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.StatusEffectRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TechniqueAbility {
    public abstract void activate(PlayerEntity player);
    public abstract Identifier getAbilityId();
    public Text getDisplayText(PlayerEntity playerEntity) { return null; }
    public int cost = 0;

    public final void use(PlayerEntity player) {
        if (!canActivate(player)) return;
        activate(player);

        if (this.getCost(player) > 0 && !player.isCreative()) CursedEnergyManager.addCursedEnergy(player, -this.getCost(player));
        CooldownManager.setAbilityCooldownIfHigher(player, this.getAbilityId(), this.getCooldown(player));
    }

    public boolean canActivate(PlayerEntity player) {
        return !CooldownManager.isOnCooldown(player, this.getAbilityId())
                && (this.getCost(player) == 0 || player.isCreative() || CursedEnergyManager.getCursedEnergy(player) >= this.getCost(player))
                && !player.hasStatusEffect(StatusEffectRegistry.STUN);
    }

    public long getCooldown(PlayerEntity player) {
        return 0;
    }

    public int getCost(PlayerEntity player) {
        return 0;
    }
}
