package com.boundless.util;

import com.boundless.ability.Ability;
import com.boundless.ability.HeldAbility;
import com.boundless.ability.TechniqueAbility;
import com.boundless.mechanics.CooldownManager;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AbilityUtils {
    public static Ability ability(Consumer<PlayerEntity> abilityLogic, int cooldown, Identifier abilityID, Identifier abilityIcon, String displayString) {
        return Ability
                .builder()
                .abilityLogic(abilityLogic)
                .cooldown(cooldown)
                .abilityID(abilityID)
                .abilityIcon(abilityIcon)
                .displayString(displayString)
                .build();
    }

    public static Ability ability(Consumer<PlayerEntity> abilityLogic, int cooldown, Identifier abilityID, String displayString) {
        return ability(abilityLogic, cooldown, abilityID, null, displayString);
    }

    public static Ability ability(Consumer<PlayerEntity> abilityLogic, int cooldown, Identifier abilityID, Identifier abilityIcon) {
        return ability(abilityLogic, cooldown, abilityID, abilityIcon, null);
    }

    public static boolean canUseAbility(PlayerEntity player, Identifier abilityID) {
        ItemStack heroStack = player.getEquippedStack(EquipmentSlot.CHEST);
        Map<Identifier, Long> cooldownData = heroStack.getOrDefault(DataComponentRegistry.COOLDOWN_DATA, Map.of());

        boolean abilityUsable = player.getWorld().getTime() > cooldownData.getOrDefault(abilityID, 0L);
        Predicate<PlayerEntity> abilityPredicate = AbilityRegistry.getAbilityFromID(abilityID).getAbilityConditional();
        abilityUsable &= abilityPredicate == null || abilityPredicate.test(player);

        return abilityUsable;
    }

    public static boolean checkAndUseTechniqueAbility(PlayerEntity player, Identifier id) {
        TechniqueAbility ability = TechniqueAbilityRegistry.getAbilityFromID(id);
        if (ability == null || !ability.canActivate(player)) return false;
        ability.use(player);
        return true;
    }
}