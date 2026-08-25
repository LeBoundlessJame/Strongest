package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.rock.RockEntity;
import com.boundless.hero.black_sparks_hero.BlackFlashAbility;
import com.boundless.util.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class RockThrowLogic {
    public static void rockThrow(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        RockEntity rock = new RockEntity(player, player.getWorld());
        rock.setVelocity(player.getRotationVector().multiply(4));
        rock.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1.2, player.getRotationVector().multiply(2).z));
        rock.setNoGravity(true);
        rock.setPitch(player.getPitch());
        rock.setYaw(player.getYaw());
        rock.setGlowing(true);
        player.getWorld().spawnEntity(rock);

        HeroUtils.getHeroStack(player).set(SwitcherHero.SECONDARY_TARGET_ID, rock.getId());

        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("aura"), 2.0f, false, false, 3000);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        // Todo: find a way to cancel this if the teleport happens
        for (int i = 0; i < 40; i++) {
            tasks.put(i, (user, action) -> {
                if (player.age % 4 == 0) {
                    EffekUtils.playEffect(BoundlessAPI.identifier("todo_aura"), player, player.getPos(), new Vec3d(3, 3, 3));
                }
                if (!player.hasStatusEffect(StatusEffects.SLOWNESS) && !player.isSneaking()) {
                    user.setVelocity(player.getRotationVector().multiply(2.0).x, player.getVelocity().y, player.getRotationVector().multiply(2.0).z);
                    user.velocityModified = true;
                    user.velocityDirty = true;
                }
            });
        }
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
    }

    public static void blackFlash(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();

        tasks.put(7, (user, heroAction) -> {
            BlackFlashAbility.blackFlash(player, 200, new Vec3d(10f, 1.0f, 10f), heroAction);
        });
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.0f, false, true, 3000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        AttackUtils.startAttackTimer(player, 10);
    }
}
