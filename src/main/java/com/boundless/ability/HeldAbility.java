package com.boundless.ability;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.mechanics.AbilityManager;
import com.boundless.mechanics.CooldownManager;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class HeldAbility extends Ability {

    @Getter
    private final int requiredHoldTime;
    @Getter
    private final String keybind;

    public HeldAbility(Consumer<PlayerEntity> abilityLogic, Predicate<PlayerEntity> abilityConditional, int cooldown, int iconHeight, int iconWidth, Identifier abilityIcon, Identifier abilityID, boolean hide, int requiredHoldTime, String keybind, String displayString) {
        super(abilityLogic, abilityConditional, cooldown, iconHeight, iconWidth, abilityIcon, abilityID, displayString, hide);
        this.requiredHoldTime = requiredHoldTime;
        this.keybind = keybind;
    }

    public void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!AbilityManager.canUseAbility(player, this.getAbilityID())) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, keybind);
        if (data.startTimestamp() == 0 || data.held()) return;

        long heldFor = player.getWorld().getTime() - data.startTimestamp();
        KeybindingUtils.endKeybindHold(player, keybind);

        if (heldFor >= requiredHoldTime) {
            Map<Identifier, Long> cooldownData = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());
            long cooldownEnd = cooldownData.getOrDefault(this.getAbilityID(), player.getWorld().getTime() + this.getCooldown());
            
            if (data.startTimestamp() >= cooldownEnd) {
                this.getAbilityLogic().accept(player);
                CooldownManager.setAbilityCooldownIfHigher(player, this.getAbilityID(), this.getCooldown());
            }
        }
    }
}
