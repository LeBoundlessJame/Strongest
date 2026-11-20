package com.boundless.util;

import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.hero.api.HeroArmor;
import com.boundless.hero.api.HeroData;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeroUtils {
    public static boolean isHero(LivingEntity user) {
        return getHeroStack(user).getItem() instanceof HeroArmor;
    }

    public static ItemStack getHeroStack(LivingEntity user) {
        return user.getEquippedStack(EquipmentSlot.CHEST);
    }

    public static HeroData getHeroData(LivingEntity user) {
        if (!isHero(user)) return null;
        HeroArmor heroArmor = (HeroArmor) user.getEquippedStack(EquipmentSlot.CHEST).getItem();
        return heroArmor.getHeroData();
    }

    public static TechniqueLoadout getTechniqueLoadout(LivingEntity user) {
        HeroData heroData = HeroUtils.getHeroData(user);
        if (heroData == null) return null;
        return heroData.getDefaultTechniqueLoadout();
    }

    public static void setLoadout(LivingEntity user, AbilityLoadout loadout) {
        HashMap<String, Identifier> loadoutMap = new HashMap<>();
        Map<String, Ability> abilities = loadout.getAbilities();
        for (Map.Entry<String, Ability> abilityEntry : new ArrayList<>(abilities.entrySet())) {
            loadoutMap.put(abilityEntry.getKey(), abilityEntry.getValue().getAbilityID());
        }
        HeroUtils.getHeroStack(user).set(DataComponentRegistry.ABILITY_LOADOUT, loadoutMap);
    }

    public static boolean combatModeEnabled(LivingEntity user) {
        return HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.COMBAT_MODE_ENABLED, false);
    }
}
