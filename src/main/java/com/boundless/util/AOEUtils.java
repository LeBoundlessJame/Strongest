package com.boundless.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class AOEUtils {
    public static List<LivingEntity> getTargetsInRadius(PlayerEntity player, World world, Vec3d center, Vec3d radius, Predicate<LivingEntity> predicate) {
        Box box = new Box(center.subtract(radius), center.add(radius));
        return world.getEntitiesByClass(LivingEntity.class, box, entity -> CombatUtils.isValidTarget(player, entity) && predicate.test(entity));
    }

    public static List<BlockPos> getBlocksInRadius(World world, Vec3d center, Vec3d radius, Predicate<BlockState> predicate) {
        BlockPos min = BlockPos.ofFloored(center.subtract(radius));
        BlockPos max = BlockPos.ofFloored(center.add(radius));

        List<BlockPos> blocks = new ArrayList<>();
        BlockPos.iterate(min, max).forEach(pos -> {
            if (predicate.test(world.getBlockState(pos))) {
                blocks.add(pos);
            }
        });

        return blocks;
    }
}
