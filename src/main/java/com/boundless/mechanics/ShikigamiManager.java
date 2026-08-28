package com.boundless.mechanics;

import com.boundless.util.RaycastUtils;
import com.boundless.util.Shikigami;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.UUID;

public class ShikigamiManager {
    public static <T extends TameableEntity & Shikigami> void toggleShikigami(PlayerEntity player, EntityType<T> shikigamiType) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return;

        NbtCompound nbt = ShikigamiNbtManager.getNbt(player, shikigamiType);

        NbtCompound result = nbt.getBoolean("summoned")
                ? desummonShikigami(player, shikigamiType)
                : summonShikigamiAtRay(player, serverWorld, shikigamiType, nbt, 16);

        if (result == null) return;

        ShikigamiNbtManager.setNbt(player, shikigamiType, result);
    }

    private static <T extends TameableEntity & Shikigami> NbtCompound summonShikigamiAtRay(PlayerEntity playerEntity, ServerWorld serverWorld, EntityType<T> shikigamiType, NbtCompound nbt, float range) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(playerEntity, range);
        if (blockHitResult == null) return null;
        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
        T shikigami = summonShikigami(playerEntity, serverWorld, shikigamiType, nbt, pos.toCenterPos());
        if (shikigami == null) return null;

        NbtCompound newNbt = new NbtCompound();
        shikigami.saveNbt(newNbt);
        newNbt.putBoolean("summoned", true);

        return newNbt;
    }

    private static <T extends TameableEntity & Shikigami> T summonShikigami(PlayerEntity playerEntity, ServerWorld serverWorld, EntityType<T> shikigamiType, NbtCompound nbt, Vec3d pos) {
        T shikigami;

        if (nbt.containsUuid("UUID")) {
            Optional<Entity> entity = EntityType.getEntityFromNbt(nbt, serverWorld);
            if (entity.isEmpty()) return null;

            shikigami = (T) entity.get();
        } else {
            shikigami = shikigamiType.create(serverWorld);
            if (shikigami == null) return null;

            shikigami.setOwner(playerEntity);
            shikigami.setPersistent();
            shikigami.setTamed(true, false);
        }

        shikigami.setPosition(pos);
        serverWorld.spawnEntity(shikigami);
        shikigami.onSummon(playerEntity);

        return shikigami;
    }

    private static <T extends TameableEntity & Shikigami> NbtCompound desummonShikigami(PlayerEntity player, EntityType<T> shikigamiType) {
        NbtCompound nbt = ShikigamiNbtManager.getNbt(player, shikigamiType);
        if (!nbt.containsUuid("UUID")) return null;

        T shikigami = getShikigami(player, shikigamiType);

        if (shikigami == null) {
            nbt.putBoolean("summoned", false);
            return nbt;
        }

        NbtCompound newNbt = new NbtCompound();
        shikigami.saveNbt(newNbt);
        shikigami.onDesummon();
        shikigami.discard();

        newNbt.putBoolean("summoned", false);
        return newNbt;
    }

    public static <T extends TameableEntity & Shikigami> T getShikigami(PlayerEntity player, EntityType<T> shikigamiType) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return null;
        NbtCompound nbt = ShikigamiNbtManager.getNbt(player, shikigamiType);

        if (!nbt.getBoolean("summoned") || !nbt.containsUuid("UUID")) return null;

        Entity entity = serverWorld.getEntity(nbt.getUuid("UUID"));
        return (T) entity;
    }
}
