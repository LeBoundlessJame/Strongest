package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static ComponentType<Long> BLACK_FLASH_TIMESTAMP = DataComponentRegistry.registerComponent("black_flash_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static Ability BLACK_FLASH = AbilityUtils.ability(BlackSparksHero::blackFlash, 5, BoundlessAPI.identifier("black_flash"), BoundlessAPI.hudPNG("black_flash"));
    public static Ability DIVERGENT_FIST = AbilityUtils.ability(BlackSparksHero::divergentFist, 5, BoundlessAPI.identifier("divergent_fist"), BoundlessAPI.hudPNG("divergent_fist"));
    public static Ability SPIN_KICK = AbilityUtils.ability(BlackSparksHero::spinKick, 20, BoundlessAPI.identifier("spin_kick"), BoundlessAPI.hudPNG("leg"));
    public static Ability GROUND_POUND = AbilityUtils.ability(BlackSparksHero::groundPound, 5, BoundlessAPI.identifier("ground_pound"), BoundlessAPI.hudPNG("black_flash"));


    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.GROUND_POUND)
                .ability("key.use", BlackSparksHero.DIVERGENT_FIST)
                .ability("key.boundless.ability_one", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_two", BlackSparksHero.SPIN_KICK)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .build();
        this.registerHero();
    }

    public static void blackFlash(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(200)
                .knockbackStrength(2)
                .impactSound(SoundRegistry.BLACK_FLASH)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("black_flash_impact"))
                .attacker(player)
                .build();
        MeleeAbility blackSparks = new MeleeAbility(data);
        blackSparks.attack(player);
    }

    public static void divergentFist(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, 15f, Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(8, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.BLACK_FLASH);
            CameraUtils.playCameraShake(player);
            CombatUtils.attack(heroAction, 1000f, Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
    }

    public static void spinKick(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(7, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, 15f, Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.25f, false, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addVelocity(player.getRotationVector().normalize().multiply(0.4f).x, player.isOnGround() ? 0.5f : 0.0f, player.getRotationVector().normalize().multiply(0.4f).z);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        player.velocityModified = true;
    }

    public static void groundPound(PlayerEntity player) {
        if (player.isOnGround()) return;
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        EntityHitResult raycastResult = RaycastUtils.raycast(player, 32);
        if (raycastResult == null) return;
        Entity entity = raycastResult.getEntity();

        player.setVelocity(player.getPos().subtract(entity.getPos()).normalize().multiply(-1.5));
        player.velocityModified = true;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 10, 2, true, false, false));

        tasks.put(10, (user, heroAction) -> {
            if (player.distanceTo(entity) < 2.0) {
                entity.kill();
            }
            //SoundUtils.playSound(player, SoundRegistry.ROCK_CRUMBLING);
            //EffekUtils.playBoundEffect(BoundlessAPI.identifier("landing_impact"), player, new Vec3d(1, 1, 1), new Vec3d(1, 1, 1));
        });
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
    }
}
