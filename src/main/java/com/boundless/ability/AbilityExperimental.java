package com.boundless.ability;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.ComponentUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;

@Getter @Setter
public class AbilityExperimental {
    public Identifier abilityID;

    public int cooldown;
    public int energyCost;

    /** If all 3 are null, then it will hide the icon **/
    public Identifier abilityIcon;
    public String displayString;
    public Integer skillSlot;

    public AbilityExperimental(Identifier abilityID) {
        this.abilityID = abilityID;
    }

    public boolean canUseAbility(PlayerEntity player) {
        return true;
    }

    public void executeAbility(PlayerEntity player) {}

    public void use(PlayerEntity player) {
        if (player.getWorld().isClient || !canUseAbility(player)) return;
        executeAbility(player);
        putOnCooldown(player, this.cooldown);
    }

    /** I leave this with a parameter instead of using this.cooldown
     * in case someone wants to do some custom cooldown stuff **/
    public void putOnCooldown(PlayerEntity player, int cooldown) {
        if (player.getWorld().isClient) return;

        ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);
        Map<Identifier, Long> updatedCooldownData = ComponentUtils.updatedCooldownMap(stack, this.abilityID, player.getWorld().getTime() + cooldown);
        stack.set(DataComponentRegistry.COOLDOWN_DATA, updatedCooldownData);
    }
}
