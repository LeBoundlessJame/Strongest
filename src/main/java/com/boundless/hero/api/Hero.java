package com.boundless.hero.api;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.HeldAbility;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.HeroRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RegistryUtils;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Hero {
    @Getter
    ArrayList<Item> armorSet;
    @Getter
    public HeroData heroData;
    @Getter
    public static LinkedHashMap<String, AbilityLoadout> ABILITY_LOADOUTS = new LinkedHashMap<>();
    @Getter
    public static ArrayList<HeldAbility> HELD_ABILITIES = new ArrayList<>();
    public static Ability COMBAT_MODE_TOGGLE = AbilityUtils.ability(Hero::combatModeToggle, 5, BoundlessAPI.identifier("combat_mode_toggle"), BoundlessAPI.hudPNG("sword"));

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
        if (player.isSprinting()) {
            heroStack.set(DataComponentRegistry.SPRINT_TICKS, heroStack.getOrDefault(DataComponentRegistry.SPRINT_TICKS, 0) + 1);
        } else {
            heroStack.set(DataComponentRegistry.SPRINT_TICKS, 0);
        }
    }

    public static void onHeroTick(PlayerEntity player) {
        for (HeldAbility heldAbility: HELD_ABILITIES) {
            heldAbility.holdTickLogic(player);
        }
    }

    public static void combatModeToggle(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.COMBAT_MODE, !HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.COMBAT_MODE, true));
    }
}
