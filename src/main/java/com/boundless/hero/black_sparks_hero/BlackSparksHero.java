package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.BlackSparksHeroRenderer;
import com.boundless.networking.payloads.evasion.EvasionClientPayload;
import com.boundless.registry.*;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static List<String> BLACK_FLASH_COMBOS = List.of("llll", "lllml", "lmmlm");

    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;

    public static Ability LIGHT_ATTACK = AbilityUtils.ability(BlackSparksHero::lightAttack, 5, BoundlessAPI.identifier("yuji_light"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(BlackSparksHero::mediumAttack, 5, BoundlessAPI.identifier("yuji_medium"), BoundlessAPI.hudPNG("leg"));
    public static Ability SPIN_KICK = AbilityUtils.ability(BlackSparksHero::spinKick, 20, BoundlessAPI.identifier("spin_kick"), BoundlessAPI.hudPNG("spin_kick"));
    public static Ability CHANNEL_CURSED_ENERGY = AbilityUtils.ability(CursedEnergyAbility::channelCursedEnergy, 4, BoundlessAPI.identifier("channel_cursed_energy"), BoundlessAPI.hudPNG("channel_cursed_energy"));

    public static ComponentType<Long> CHANNEL_CURSED_ENERGY_TIMESTAMP = DataComponentRegistry.registerComponent("channel_cursed_energy_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> MINIGAME_START_TIMESTAMP = DataComponentRegistry.registerComponent("minigame_start_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> MINIGAME_END_TIMESTAMP = DataComponentRegistry.registerComponent("minigame_end_timestamp", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<String> TARGET_MINIGAME_COMBO = DataComponentRegistry.registerComponent("target_minigame_combo", builder -> ComponentType.<String>builder().codec(Codec.STRING));
    public static ComponentType<String> CURRENT_MINIGAME_COMBO = DataComponentRegistry.registerComponent("current_minigame_combo", builder -> ComponentType.<String>builder().codec(Codec.STRING));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.80f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 10, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.LIGHT_ATTACK)
                .ability("key.use", BlackSparksHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", BlackSparksHero.DODGE)
                .ability("key.boundless.ability_two", BlackSparksHero.SPIN_KICK)
                .ability("key.boundless.ability_three", BlackSparksHero.CHANNEL_CURSED_ENERGY)
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
                .build();
        this.registerHero();
    }

    public static void lightAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        if (updateMinigameCombo(player, "l")) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        if (CursedEnergyAbility.channelCursedEnergyActive(player)) {
            startMinigame(player, "l");
            HeroUtils.getHeroStack(player).set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
        }

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> hook = (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.lightAttack.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        };
        tasks.put(4, hook);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, attackCount % 2 == 0, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, 4);
    }

    public static void mediumAttack(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;

        if (updateMinigameCombo(player, "m")) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(DataComponentRegistry.ATTACK_COUNT, player, 0);

        if (CursedEnergyAbility.channelCursedEnergyActive(player)) {
            CursedEnergyAbility.divergentFist(player);
            HeroUtils.getHeroStack(player).set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
        } else {
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            BiConsumer<PlayerEntity, HeroActionEntity> kick = (user, heroAction) -> {
                SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
                CombatUtils.attack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
                CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                    target.addVelocity(0, 0.5f, 0);
                    target.velocityModified = true;
                });
            };
            tasks.put(4, kick);
            tasks.put(8, kick);
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("double_kick"), 1.0f, attackCount % 2 == 0, true, 2000);
            ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
            AttackUtils.startAttackTimer(player, 8);
        }
    }

    public static void spinKick(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(7, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.knockbackAttack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.0f, false, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addVelocity(player.getRotationVector().normalize().multiply(0.4f).x, player.isOnGround() ? 0.5f : 0.0f, player.getRotationVector().normalize().multiply(0.4f).z);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        player.velocityModified = true;
        AttackUtils.startAttackTimer(player, 10);
    }

    // Returns true if it is the final hit of the minigame combo
    // Todo: rework, I don't like the vagueness of this
    public static boolean updateMinigameCombo(PlayerEntity player, String attack) {
        ItemStack stack = HeroUtils.getHeroStack(player);

        String currentCombo = stack.getOrDefault(BlackSparksHero.CURRENT_MINIGAME_COMBO, "");
        String targetCombo = stack.getOrDefault(BlackSparksHero.TARGET_MINIGAME_COMBO, "");

        stack.set(BlackSparksHero.CURRENT_MINIGAME_COMBO, currentCombo + attack);
        currentCombo = currentCombo + attack;

        /*
        boolean withinTimePeriod = player.getWorld().getTime() <= stack.getOrDefault(BlackSparksHero.MINIGAME_END_TIMESTAMP, 0L);

        if (!withinTimePeriod) {
            endMinigame(player);
            return false;
        }
         */

        if (stack.getOrDefault(BlackSparksHero.CURRENT_MINIGAME_COMBO, "").equals(stack.getOrDefault(BlackSparksHero.TARGET_MINIGAME_COMBO, ""))) {
            CursedEnergyAbility.blackFlash(player);
            endMinigame(player);
            return true;
        } else if (currentCombo.length() > targetCombo.length() || !targetCombo.startsWith(currentCombo)) {
            endMinigame(player);
        }
        return false;
    }

    public static void startMinigame(PlayerEntity player, String beginningAttack) {
        ItemStack stack = HeroUtils.getHeroStack(player);

        stack.set(BlackSparksHero.MINIGAME_START_TIMESTAMP, player.getWorld().getTime());
        stack.set(BlackSparksHero.MINIGAME_END_TIMESTAMP, player.getWorld().getTime() + CursedEnergyAbility.MINIGAME_DURATION);
        stack.set(BlackSparksHero.TARGET_MINIGAME_COMBO, BLACK_FLASH_COMBOS.get(player.getRandom().nextInt(BLACK_FLASH_COMBOS.size())));
        stack.set(BlackSparksHero.CURRENT_MINIGAME_COMBO, beginningAttack);
    }

    public static void endMinigame(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);

        stack.set(BlackSparksHero.MINIGAME_START_TIMESTAMP, 0L);
        stack.set(BlackSparksHero.MINIGAME_END_TIMESTAMP, 0L);
        stack.set(BlackSparksHero.TARGET_MINIGAME_COMBO, "");
        stack.set(BlackSparksHero.CURRENT_MINIGAME_COMBO, "");
    }

    public static Ability DODGE = Ability.builder()
            .abilityID(BoundlessAPI.identifier("dash"))
            .abilityIcon(BoundlessAPI.hudPNG("dash"))
            .cooldown(60)
            .abilityLogic((player) -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVULNERABILITY_EFFECT, 20, 0, true, false, false));
                if (!player.getWorld().isClient) {
                    ServerPlayNetworking.send((ServerPlayerEntity) player, new EvasionClientPayload(player.getUuid()));
                }
            })
            .build();
}
