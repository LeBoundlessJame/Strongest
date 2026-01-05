package com.boundless.ability;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HeldAbility extends Ability {

    private final int requiredHoldTime;
    private final String keybind;

    public HeldAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, hide);
        this.requiredHoldTime = requiredHoldTime;
        this.keybind = keybind;
    }

    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, keybind);
        if (data.startTimestamp() == 0 || data.held()) return;

        long heldFor = player.getWorld().getTime() - data.startTimestamp();
        KeybindingUtils.endKeybindHold(player, keybind);

        if (heldFor >= requiredHoldTime) {
            Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
            long cooldownEnd = cooldownData.get(this.getAbilityID());

            if (data.startTimestamp() >= cooldownEnd) {
                this.getAbilityLogic().accept(player);
                AbilityUtils.setAbilityCooldown(player, this.getAbilityID(), this.getCooldown());
            }
        }
    }

}
