package com.boundless.ability;

import com.boundless.mechanics.CooldownManager;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.StatusEffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class TechniqueAbility {
    public abstract void activate(LivingEntity user);
    public abstract Identifier getId();
    public Text getDisplayText(LivingEntity user) { return null; }
    public int cost = 0;

    public final void use(LivingEntity user) {
        if (!canActivate(user)) return;
        activate(user);

        boolean isCreative = user instanceof PlayerEntity player && player.isCreative();

        if (this.getCost(user) > 0 && !isCreative) CursedEnergyManager.addCursedEnergy(user, -this.getCost(user));
        CooldownManager.setAbilityCooldownIfHigher(user, this.getId(), this.getCooldown(user));
    }

    public boolean canActivate(LivingEntity user) {
        boolean isCreative = user instanceof PlayerEntity player && player.isCreative();

        return !CooldownManager.isOnCooldown(user, this.getId())
                && (this.getCost(user) == 0 || isCreative || CursedEnergyManager.getCursedEnergy(user) >= this.getCost(user))
                && !user.hasStatusEffect(StatusEffectRegistry.STUN);
    }

    public long getCooldown(LivingEntity user) {
        return 0;
    }

    public int getCost(LivingEntity user) {
        return 0;
    }
}
