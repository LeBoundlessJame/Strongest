package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class CursedEnergyAbility {
    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;
    public static long MINIGAME_DURATION = 60;
    public static long CE_TIME_WINDOW = 60;

    public static void channelCursedEnergy(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, channelCursedEnergyActive(player)
                ? player.getWorld().getTime()
                : player.getWorld().getTime() + CE_TIME_WINDOW);
    }

    public static void divergentFist(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistPunch.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(8, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.BLACK_FLASH);
            CameraUtils.playCameraShake(player);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistImpact.get(), Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
        AttackUtils.startAttackTimer(player, 8);
    }

    public static void blackFlash(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(DAMAGE.blackFlash.get())
                .knockbackStrength(2)
                .impactSound(SoundRegistry.BLACK_FLASH)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("black_flash_impact"))
                .attacker(player)
                .build();
        MeleeAbility blackSparks = new MeleeAbility(data);
        blackSparks.attack(player);
        CameraUtils.playCameraShake(player);
        AttackUtils.startAttackTimer(player, 6);

        if (player.getWorld().isClient) return;
        player.sendMessage(Text.of("§c§l§ka§c §c§lKOKUSEN! §c§l§ka§c"), true);
    }

    public static boolean channelCursedEnergyActive(PlayerEntity player) {
        return player.getWorld().getTime() < HeroUtils.getHeroStack(player).getOrDefault(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
    }

    public static boolean blackFlashMinigameActive(PlayerEntity player) {
        return player.getWorld().getTime() < HeroUtils.getHeroStack(player).getOrDefault(BlackSparksHero.MINIGAME_TIMESTAMP, player.getWorld().getTime());
    }
}
