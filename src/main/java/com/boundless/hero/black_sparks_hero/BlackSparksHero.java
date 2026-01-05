package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.HeldAbility;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.BlackSparksHeroRenderer;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static BlackSparksHeroConfig CONFIG = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG;
    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = CONFIG.abilityDamageConfig;
    public static BlackSparksHeroConfig.AbilityCooldownConfig COOLDOWNS = CONFIG.abilityCooldownConfig;

    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(BlackSparksHero::mediumAttack, COOLDOWNS.mediumAttack.get(), BoundlessAPI.identifier("yuji_medium"), BoundlessAPI.hudPNG("leg"));
    public static Ability SPIN_KICK = AbilityUtils.ability(BlackSparksHero::spinKick, COOLDOWNS.spinKick.get(), BoundlessAPI.identifier("spin_kick"), BoundlessAPI.hudPNG("spin_kick"));
    public static Ability BLACK_FLASH = AbilityUtils.ability(BlackSparksHero::startBlackFlashMinigame, COOLDOWNS.channelCursedEnergy.get(), BoundlessAPI.identifier("black_flash"), BoundlessAPI.hudPNG("black_flash"));

    public static HeldAbility LIGHT_ATTACK = new DivergentLightAttackAbility(CursedEnergyAbility::divergentFist, null, COOLDOWNS.lightAttack.get(), 22, 22, BoundlessAPI.hudPNG("arm"), BoundlessAPI.identifier("yuji_light"), false, 10, "key.attack");
    public static HeldAbility DASH = new DashAbility(DashAbility::superDash, null, COOLDOWNS.dodge.get(), 22, 22, BoundlessAPI.hudPNG("dash"), BoundlessAPI.identifier("dash"), false, 10, "key.boundless.ability_one");

    public static ComponentType<Long> CHANNEL_CURSED_ENERGY_TIMESTAMP = DataComponentRegistry.registerComponent("channel_cursed_energy_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> MINIGAME_START_TIMESTAMP = DataComponentRegistry.registerComponent("minigame_start_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> MINIGAME_END_TIMESTAMP = DataComponentRegistry.registerComponent("minigame_end_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<String> TARGET_MINIGAME_COMBO = DataComponentRegistry.registerComponent("target_minigame_combo", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static ComponentType<String> CURRENT_MINIGAME_COMBO = DataComponentRegistry.registerComponent("current_minigame_combo", builder -> ComponentType.<String>builder().codec(Codec.STRING));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), CONFIG.damageReduction.get(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 10, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.LIGHT_ATTACK)
                .ability("key.use", BlackSparksHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", BlackSparksHero.DASH)
                .ability("key.boundless.ability_two", BlackSparksHero.SPIN_KICK)
                .ability("key.boundless.ability_three", BlackSparksHero.BLACK_FLASH)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BlackSparksHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(BlackSparksHeroRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .heldKeybind("key.boundless.ability_one")
                .heldKeybind("key.attack")
                .build();
        this.registerHero();
    }

    public static void mediumAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        if (CursedEnergyAbility.updateMinigameCombo(player, "m")) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> kick = (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.mediumAttackPerHit.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
            CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                target.addVelocity(0, 0.5f, 0);
                target.velocityModified = true;
                target.timeUntilRegen = 0;
            });
        };
        tasks.put(4, kick);
        tasks.put(8, kick);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("double_kick"), 1.0f, attackCount % 2 == 0, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, 8);
    }

    public static void spinKick(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(7, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.knockbackAttack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.0f, false, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        AttackUtils.startAttackTimer(player, 10);
    }

    public static void startBlackFlashMinigame(PlayerEntity player) {
        CursedEnergyAbility.startMinigame(player, "");
    }
}
