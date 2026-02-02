package com.boundless.ability;

import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Math;

public class Grab {
    public static ComponentType<Long> GRAB_START = DataComponentRegistry.registerComponent("grab_start",builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static ComponentType<Long> GRAB_END = DataComponentRegistry.registerComponent("grab_end",builder -> ComponentType.<Long>builder().codec(Codec.LONG));

    public static Vec3d suplex(Entity user, Vec3d initialOffset, float delta) {
        Vec3d targetOffset = user.getRotationVector().normalize().multiply(-5).multiply(1, 0, 1);
        //if (user instanceof PlayerEntity player) player.sendMessage(Text.of(String.valueOf(delta)));

        double x = Math.lerp(initialOffset.x, targetOffset.x, delta);
        double y = Math.lerp(3, targetOffset.y, delta);
        double z = Math.lerp(initialOffset.z, targetOffset.z, delta);

        return new Vec3d(x, y, z);
    }

    public static void initialize() {}
}
