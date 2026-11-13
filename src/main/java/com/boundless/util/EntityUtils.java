package com.boundless.util;

import com.boundless.entity.nail.NailEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtils {
    public static <T extends Entity> T createPlayerProjectile(PlayerEntity player, T projectile) {
        projectile.setVelocity(player.getRotationVector().multiply(4));
        projectile.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1, player.getRotationVector().multiply(2).z));
        projectile.setNoGravity(true);
        projectile.setPitch(player.getPitch());
        projectile.setYaw(player.getYaw());
        return projectile;
    }
}
