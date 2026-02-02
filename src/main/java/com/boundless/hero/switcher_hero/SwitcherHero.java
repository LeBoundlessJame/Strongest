package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class SwitcherHero extends Hero {
    public static SwitcherConfig CONFIG = ConfigRegistry.HERO_CONFIG.SWITCHER_CONFIG;
    public static SwitcherConfig.AbilityDamageConfig DAMAGE = CONFIG.abilityDamageConfig;
    public static SwitcherConfig.AbilityCooldownConfig COOLDOWNS = CONFIG.abilityCooldownConfig;

    public static Ability LIGHT_ATTACK = AbilityUtils.ability(SwitcherLightLogic::lightAttack, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("switcher_light_attack"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(SwitcherMediumLogic::mediumAttack, COOLDOWNS.mediumAttack.get(), BoundlessAPI.identifier("todo_medium"), BoundlessAPI.hudPNG("leg"));

    public static Ability BOOGIE = AbilityUtils.ability(BoogieLogic::standardSwap, COOLDOWNS.clap.get(), BoundlessAPI.identifier("boogie"), BoundlessAPI.hudPNG("clap"), "Boogie");
    public static Ability ROCK_THROW = AbilityUtils.ability(RockThrowLogic::rockThrow, COOLDOWNS.rockThrow.get(), BoundlessAPI.identifier("rock_throw"), BoundlessAPI.hudPNG("rock_throw"), "Rock Throw");
    public static Ability TARGET_SELECT = AbilityUtils.ability(TargetSelectMenu::openTargetSelectMenu, COOLDOWNS.targetSelect.get(), BoundlessAPI.identifier("boogie_menu"), BoundlessAPI.hudPNG("select_target"), "Select Target");
    public static Ability SUPLEX = AbilityUtils.ability(GrabLogic::suplex, COOLDOWNS.suplex.get(), BoundlessAPI.identifier("suplex"), BoundlessAPI.hudPNG("divergent_fist"), "Suplex");

    public static Ability SWAP_TWO = AbilityUtils.ability(BoogieLogic::swapTwo, COOLDOWNS.clap.get(), BoundlessAPI.identifier("swap_two"), "Swap Two");
    public static Ability SWAP_WITH_PRIMARY = AbilityUtils.ability(BoogieLogic::swapWithPrimary, COOLDOWNS.clap.get(), BoundlessAPI.identifier("swap_with_primary"), "Swap -> Primary");
    public static Ability SWAP_WITH_SECONDARY = AbilityUtils.ability(BoogieLogic::swapWithSecondary, COOLDOWNS.clap.get(), BoundlessAPI.identifier("swap_with_secondary"), "Swap -> Secondary");

    public static ComponentType<Long> LAST_REVIVE_TIMESTAMP = DataComponentRegistry.registerComponent("last_revive_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> TIME_UNTIL_NEXT_REVIVE = DataComponentRegistry.registerComponent("time_until_next_revive", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> CLAP_SELECT_TIME = DataComponentRegistry.registerComponent("clap_select_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> TARGET_SELECT_TIME = DataComponentRegistry.registerComponent("target_select_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<String> BOOGIE_SELECTION = DataComponentRegistry.registerComponent("boogie_selection", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static ComponentType<Integer> PRIMARY_TARGET_ID = DataComponentRegistry.registerComponent("primary_target_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static ComponentType<Integer> SECONDARY_TARGET_ID = DataComponentRegistry.registerComponent("secondary_target_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public SwitcherHero() {
        ABILITY_LOADOUTS.put("LOADOUT_1", SwitcherStats.LOADOUT_1);
        ABILITY_LOADOUTS.put("LOADOUT_2", SwitcherStats.LOADOUT_2);

        this.heroData = HeroData.builder()
                .name("switcher_hero")
                .textureIdentifier(BoundlessAPI.textureID("switcher"))
                .defaultAbilityLoadout(SwitcherStats.LOADOUT_1)
                .attributes(SwitcherStats.ATTRIBUTES)
                .hudRenderer(SwitcherHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(HeroArmorRenderer::new)
                .armorRenderer(SwitcherRenderer::new)
                .tickHandler(BoogieLogic::tick)
                .modelIdentifier(BoundlessAPI.modelID("switcher"))
                .build();
        this.registerHero();
    }
}
