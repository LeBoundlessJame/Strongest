package com.boundless.ability;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.ComponentUtils;
import com.boundless.util.MeterUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

@Getter @Setter
public abstract class Ability {
    public Identifier id;

    public int cooldown = 0;
    public int energyCost = 0;
    public int abilityDuration = 0;

    /** If all 3 are null, then it will hide the icon **/
    public Identifier icon;
    public String displayText;
    public Integer skillSlot;

    public Ability(Identifier id) {
        this.id = id;
    }

    public boolean canUseAbility(PlayerEntity player) {
        boolean canUse = !player.getWorld().isClient;
        canUse &= AbilityUtils.nextAbilityUsable(player);
        canUse &= MeterUtils.getRemainingMeter(player) >= energyCost;
        canUse &= !this.isOnCooldown(player);
        return canUse;
    }

    public abstract void executeAbility(PlayerEntity player);

    public void use(PlayerEntity player) {
        if (player.getWorld().isClient || !canUseAbility(player)) return;
        executeAbility(player);
        putOnCooldown(player, this.cooldown);
        MeterUtils.consumeMeter(player, this.energyCost);
        AbilityUtils.setNextAbilityUseTime(player, this.abilityDuration);
    }

    /** I leave this with a parameter instead of using this.cooldown
     * in case someone wants to do some custom cooldown stuff **/
    public void putOnCooldown(PlayerEntity player, int cooldown) {
        if (player.getWorld().isClient) return;

        ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
        Map<Identifier, Long> updatedCooldownData = ComponentUtils.updatedCooldownMap(stack, this.getId(), player.getWorld().getTime() + cooldown);
        stack.set(DataComponentRegistry.COOLDOWN_DATA, updatedCooldownData);
    }

    public boolean isOnCooldown(PlayerEntity player) {
        return AbilityUtils.isOnCooldown(player, this.getId());
    }
}
