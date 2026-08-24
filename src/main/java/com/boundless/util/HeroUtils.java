package com.boundless.util;

import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.HeroArmor;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeroUtils {
    public static boolean isHero(PlayerEntity player) {
        return getHeroStack(player).getItem() instanceof HeroArmor;
    }

    public static ItemStack getHeroStack(PlayerEntity player) {
        return player.getEquippedStack(EquipmentSlot.CHEST);
    }

    public static HeroData getHeroData(PlayerEntity player) {
        if (!isHero(player)) return null;
        HeroArmor heroArmor = (HeroArmor) player.getEquippedStack(EquipmentSlot.CHEST).getItem();
        return heroArmor.getHeroData();
    }

    public static void setLoadout(PlayerEntity player, AbilityLoadout loadout) {
        HashMap<String, Identifier> loadoutMap = new HashMap<>();
        Map<String, Ability> abilities = loadout.getAbilities();
        for (Map.Entry<String, Ability> abilityEntry : new ArrayList<>(abilities.entrySet())) {
            loadoutMap.put(abilityEntry.getKey(), abilityEntry.getValue().getAbilityID());
        }
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.ABILITY_LOADOUT, loadoutMap);
    }

    public static boolean combatModeEnabled(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE_ENABLED, false);
    }
}
