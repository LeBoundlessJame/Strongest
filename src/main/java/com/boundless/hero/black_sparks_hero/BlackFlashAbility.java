package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackFlashAbility {
    public static List<String> BLACK_FLASH_COMBOS = List.of("llll", "lllml", "lmmlm");

    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;
    public static BlackSparksHeroConfig CONFIG = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG;

    public static void divergentFist(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistPunch.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(15, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);

            CameraUtils.playCameraShake(player);
            CombatUtils.perEnemyLogic(heroAction, (attacker, livingEntity) -> {
                livingEntity.timeUntilRegen = 0;
            });
            CombatUtils.attack(heroAction, DAMAGE.divergentFistImpact.get(), Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
        AttackUtils.startAttackTimer(player, 10);
    }

    public static void blackFlash(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;

            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);
            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_3);
            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_HEAVY);

            CameraUtils.playCameraShake(player);
            CombatUtils.perEnemyLogic(heroAction, (attacker, livingEntity) -> {
                livingEntity.timeUntilRegen = 0;
                CombatUtils.knockback(attacker, livingEntity, 2.0f);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, CONFIG.impactFrameDuration.get(), 1, false, false, false));
            });
            CombatUtils.attack(heroAction, DAMAGE.blackFlash.get(), Optional.of(BoundlessAPI.identifier("black_flash_impact")));
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, CONFIG.impactFrameDuration.get(), 1, false, false, false));
        });
        Action impact = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), true, 5000);
        ActionUtils.performAction(player, impact);
        AttackUtils.startAttackTimer(player, 6);

        if (player.getWorld().isClient) return;
        player.sendMessage(Text.of("§c§l§ka§c §c§lKOKUSEN! §c§l§ka§c"), true);
    }

    public static boolean blackFlashMinigameActive(PlayerEntity player) {
        return player.getWorld().getTime() < HeroUtils.getHeroStack(player).getOrDefault(BlackSparksHero.MINIGAME_END_TIMESTAMP, 0L);
    }

    // Returns true if it is the final hit of the minigame combo
    // Todo: rework, I don't like the vagueness of this
    public static boolean updateMinigameCombo(PlayerEntity player, String attack) {
        ItemStack stack = HeroUtils.getHeroStack(player);

        String currentCombo = stack.getOrDefault(BlackSparksHero.CURRENT_MINIGAME_COMBO, "");
        String targetCombo = stack.getOrDefault(BlackSparksHero.TARGET_MINIGAME_COMBO, "");

        stack.set(BlackSparksHero.CURRENT_MINIGAME_COMBO, currentCombo + attack);
        currentCombo = currentCombo + attack;

        boolean withinTimePeriod = player.getWorld().getTime() <= stack.getOrDefault(BlackSparksHero.MINIGAME_END_TIMESTAMP, 0L);

        if (!withinTimePeriod) {
            endMinigame(player);
            return false;
        }

        if (stack.getOrDefault(BlackSparksHero.CURRENT_MINIGAME_COMBO, "").equals(stack.getOrDefault(BlackSparksHero.TARGET_MINIGAME_COMBO, ""))) {
            BlackFlashAbility.blackFlash(player);
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
        stack.set(BlackSparksHero.MINIGAME_END_TIMESTAMP, player.getWorld().getTime() + CONFIG.blackFlashTimeWindow.get());
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
}
