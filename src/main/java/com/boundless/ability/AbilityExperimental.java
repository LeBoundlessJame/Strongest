package com.boundless.ability;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.ComponentUtils;
import lombok.Setter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;

@Setter
public class AbilityExperimental {
    public Consumer<PlayerEntity> logic;
    public Identifier abilityID;

    public int cooldown;
    public int energyCost;

    public AbilityExperimental(Consumer<PlayerEntity> logic, Identifier abilityID) {
        this.logic = logic;
        this.abilityID = abilityID;
    }

    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public void use(PlayerEntity player) {
        if (player.getWorld().isClient || !canUse(player)) return;
        logic.accept(player);
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
