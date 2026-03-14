package com.boundless.hero.api;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.HeldAbility;
import com.boundless.combat.Combo;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.HeroRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.RegistryUtils;
import lombok.Getter;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Hero {
    public static List<Combo> COMBOS;

    @Getter
    ArrayList<Item> armorSet;
    @Getter
    public HeroData heroData;
    @Getter
    public LinkedHashMap<String, AbilityLoadout> ABILITY_LOADOUTS = new LinkedHashMap<>();
    @Getter
    public static ArrayList<HeldAbility> HELD_ABILITIES = new ArrayList<>();

    public void registerHero() {
        this.armorSet = RegistryUtils.registerHero(this);

        if (!ABILITY_LOADOUTS.isEmpty()) {
            for (AbilityLoadout loadout : ABILITY_LOADOUTS.values()) {
                for (Map.Entry<String, Ability> abilityEntry : loadout.getAbilities().entrySet()) {
                    Ability ability = abilityEntry.getValue();
                    if (!AbilityRegistry.ABILITIES.containsKey(ability.getAbilityID())) {
                        BoundlessAPI.LOGGER.info("Registered " + ability.getAbilityID() + " ability");
                    }
                    AbilityRegistry.ABILITIES.putIfAbsent(ability.getAbilityID(), abilityEntry.getValue());
                }

                for (Ability ability: loadout.getAbilities().values()) {
                    if (ability instanceof HeldAbility heldAbility) {
                        HELD_ABILITIES.add(heldAbility);
                    }
                }
            }
        }

        HeroRegistry.HEROES.add(this);
        BoundlessAPI.LOGGER.info("Registered " + this.heroData.getName());
    }

    public static void heroSprintHandler(PlayerEntity player) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        if (player.isSprinting() && !player.hasStatusEffect(StatusEffectRegistry.LIMITED_SPEED)) {
            heroStack.set(DataComponentRegistry.SPRINT_TICKS, heroStack.getOrDefault(DataComponentRegistry.SPRINT_TICKS, 0) + 1);
        } else {
            heroStack.set(DataComponentRegistry.SPRINT_TICKS, 0);
        }
    }

    // Todo: make this figure configurable
    public static void regenTick(PlayerEntity player) {
        player.heal(player.getMaxHealth() / 50);
    }

    public static void onHeroTick(PlayerEntity player) {
        for (HeldAbility heldAbility: HELD_ABILITIES) {
            heldAbility.holdTickLogic(player);
        }
    }
}
