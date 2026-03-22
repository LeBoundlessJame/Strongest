package com.boundless.ability;

import com.boundless.BoundlessAPI;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;

public class BlockLogic {
    public static void tick(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        blockAnimation(player);

        if (!player.hasStatusEffect(StatusEffectRegistry.GUARD_BREAK) && player.age % 20 == 0) {
            DataComponentUtils.incrementFloat(DataComponentRegistry.BLOCK_HP, player, 2f, 0, 150f);
        }

        if (isBlocking(player)) {
            DataComponentUtils.incrementInt(DataComponentRegistry.BLOCK_TICKS, player, 1);
        } else {
            HeroUtils.getHeroStack(player).set(DataComponentRegistry.BLOCK_TICKS, 0);
        }

        if (!isBlocking(player)) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 1, 1, false, false));
        MeleeUtils.disorient(player, 2);
        player.setSprinting(false);
    }

    public static void blockAnimation(PlayerEntity player) {
        if (AnimationUtils.getLastServerTriggeredAnimation(player).equals(BoundlessAPI.identifier("block_2")) && !isBlocking(player)) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("null"), 1.0f, false, false, 9999);
        } else if (isBlocking(player) && KeybindingUtils.getHeldTime(player, "key.use") == 1L) {
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("block_2"), 2.0f, false, true, 9999);
        }
    }

    public static boolean isBlocking(PlayerEntity player) {
        return KeybindingUtils.isHoldingKey(player, "key.use") && !player.hasStatusEffect(StatusEffectRegistry.GUARD_BREAK);
    }

    public static boolean shouldBlockDamage(DamageSource source, PlayerEntity player) {
        boolean bl = source.getSource() instanceof PersistentProjectileEntity persistentProjectileEntity && persistentProjectileEntity.getPierceLevel() > 0;

        // Todo: add conditional to check if ability is blockable
        if (isBlocking(player) && !bl) {
            Vec3d vec3d = source.getPosition();
            if (vec3d != null) {
                Vec3d vec3d2 = player.getRotationVector(0.0F, player.getHeadYaw());
                Vec3d vec3d3 = vec3d.relativize(player.getPos());
                vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();
                return vec3d3.dotProduct(vec3d2) < 0.0;
            }
        }

        return false;
    }
}
