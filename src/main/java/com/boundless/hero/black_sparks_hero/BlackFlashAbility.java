package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackFlashAbility {
    public static BrawlerHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;
    public static BrawlerHeroConfig CONFIG = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG;

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
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, 4, 4, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 6, 4, true, false, false));
        });
        Action impact = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), true, 5000);
        ActionUtils.performAction(player, impact);
        AttackUtils.startAttackTimer(player, 6);

        if (player.getWorld().isClient) return;
        player.sendMessage(Text.of("§c§l§ka§c §c§lKOKUSEN! §c§l§ka§c"), true);
    }

    public static float getBlackFlashChance(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(StrongestComponents.BLACK_FLASH_CHANCE, 0.01f);
    }
}
