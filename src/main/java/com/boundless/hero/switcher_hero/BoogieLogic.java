package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.rock.RockEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BoogieLogic {

    public static HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> BOOGIE_MAP = getBoogieMap();

    public static HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> getBoogieMap() {
        HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> boogieMap = new HashMap<>();
        boogieMap.put("standard", BoogieLogic::standardSwap);
        return boogieMap;
    }

    public static void swapEntities(Entity first, Entity second) {
        Vec3d firstPos = first.getPos();
        Vec3d secondPos = second.getPos();

        if (first instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.identifier("energy_spark"));
        }

        second.requestTeleport(firstPos.x, firstPos.y, firstPos.z);
        first.requestTeleport(secondPos.x, secondPos.y, secondPos.z);

        if (second instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.identifier("energy_spark"));
            first.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, secondPos);
        }

        if (second instanceof RockEntity rock && rock.getOwner() == first && rock.getOwner() instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 0, false, false, false));
            BoogieLogic.blackFlash(player);
        }
    }

    public static void standardSwap(PlayerEntity player, HeroActionEntity heroAction) {
        SoundUtils.playSound(player, SoundRegistry.CLAP_1, 8, 12);

        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        BoogieLogic.swapEntities(player, target);
    }

    public static void clap(PlayerEntity user) {
        if (user.getWorld().isClient()) return;

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("clap"), 1.0f, false, true, 3000);

        tasks.put(3, BOOGIE_MAP.getOrDefault("standard", BoogieLogic::standardSwap));
        ActionUtils.performAction(user, Action.builder().scheduledTasks(tasks).build());
    }

    public static void rockThrow(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        RockEntity rock = new RockEntity(player, player.getWorld());
        rock.setVelocity(player.getRotationVector().multiply(5));
        rock.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1.2, player.getRotationVector().multiply(2).z));
        rock.setNoGravity(true);
        rock.setPitch(player.getPitch());
        rock.setYaw(player.getYaw());
        rock.setGlowing(true);
        player.getWorld().spawnEntity(rock);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("aura"), 2.0f, false, false, 3000);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        // Todo: find a way to cancel this if the teleport happens
        for (int i = 0; i < 40; i++) {
            tasks.put(i, (user, action) -> {
                if (player.age % 4 == 0) {
                    EffekUtils.playEffect(BoundlessAPI.identifier("stars"), player, player.getPos(), new Vec3d(3, 3, 3));
                }
                if (!player.hasStatusEffect(StatusEffects.SLOWNESS)) {
                    user.setVelocity(player.getRotationVector().multiply(2.0).x, player.getVelocity().y, player.getRotationVector().multiply(2.0).z);
                    user.velocityModified = true;
                    user.velocityDirty = true;
                }
            });
        }
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());

        //rock.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 1.0F);
    }

    public static void blackFlash(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        tasks.put(7, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_2);
            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_3);
            SoundUtils.playSound(player, SoundRegistry.ENERGY_IMPACT_HEAVY);

            CameraUtils.playCameraShake(player);
            CombatUtils.perEnemyLogic(heroAction, (attacker, livingEntity) -> {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, 4, 1, false, false, false));

                livingEntity.timeUntilRegen = 0;
                CombatUtils.strongKnockback(attacker, livingEntity, 10.0f);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, 4, 1, false, false, false));
            });
            CombatUtils.attack(heroAction, 400, Optional.of(BoundlessAPI.identifier("black_flash_impact")));
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.0f, false, true, 3000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        AttackUtils.startAttackTimer(player, 10);
    }
}
