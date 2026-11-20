package com.boundless.mechanics;

import com.boundless.util.RaycastUtils;
import com.boundless.util.Shikigami;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class ShikigamiManager {
    public static <T extends TameableEntity & Shikigami> void toggleShikigami(LivingEntity user, EntityType<T> shikigamiType) {
        if (!(user.getWorld() instanceof ServerWorld)) return;

        NbtCompound nbt = ShikigamiNbtManager.getNbt(user, shikigamiType);

        if (nbt.getBoolean("summoned")) {
            desummonShikigami(user, shikigamiType);
        } else {
            summonShikigamiAtRay(user, shikigamiType, 16);
        }
    }

    // Todo: might make this a boolean later in case we want unsuccessful summons to not consume CE, but we'll see
    public static <T extends TameableEntity & Shikigami> void summonShikigamiAtRay(LivingEntity user, EntityType<T> shikigamiType, float range) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(user, range);
        if (blockHitResult == null) return;
        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

        summonShikigamiAt(user, shikigamiType, pos.toCenterPos());
    }

    public static <T extends TameableEntity & Shikigami> T summonShikigami(LivingEntity user, ServerWorld serverWorld, EntityType<T> shikigamiType, NbtCompound nbt, Vec3d pos) {
        T shikigami;

        if (nbt.containsUuid("UUID")) {
            Optional<Entity> entity = EntityType.getEntityFromNbt(nbt, serverWorld);
            if (entity.isEmpty()) return null;

            shikigami = (T) entity.get();
        } else {
            shikigami = shikigamiType.create(serverWorld);
            if (shikigami == null) return null;

            shikigami.setOwnerUuid(user.getUuid());
            shikigami.setPersistent();
            shikigami.setTamed(true, false);
        }

        shikigami.setPosition(pos);
        serverWorld.spawnEntity(shikigami);
        shikigami.onSummon(user);

        return shikigami;
    }

    public static <T extends TameableEntity & Shikigami> void desummonShikigami(LivingEntity user, EntityType<T> shikigamiType) {
        NbtCompound nbt = ShikigamiNbtManager.getNbt(user, shikigamiType);
        if (!nbt.containsUuid("UUID")) return;

        T shikigami = getShikigami(user, shikigamiType);

        if (shikigami == null) {
            nbt.putBoolean("summoned", false);
            ShikigamiNbtManager.setNbt(user, shikigamiType, nbt);
            return;
        }

        NbtCompound newNbt = new NbtCompound();
        shikigami.saveNbt(newNbt);
        shikigami.onDesummon();
        shikigami.discard();

        newNbt.putBoolean("summoned", false);
        ShikigamiNbtManager.setNbt(user, shikigamiType, newNbt);
    }

    public static <T extends TameableEntity & Shikigami> T getShikigami(LivingEntity user, EntityType<T> shikigamiType) {
        if (!(user.getWorld() instanceof ServerWorld serverWorld)) return null;
        NbtCompound nbt = ShikigamiNbtManager.getNbt(user, shikigamiType);

        if (!nbt.getBoolean("summoned") || !nbt.containsUuid("UUID")) return null;

        Entity entity = serverWorld.getEntity(nbt.getUuid("UUID"));
        return (T) entity;
    }

    public static <T extends TameableEntity & Shikigami> T summonShikigamiAt(LivingEntity user, EntityType<T> shikigamiType, Vec3d pos) {
        if (!(user.getWorld() instanceof ServerWorld serverWorld)) return null;

        NbtCompound nbt = ShikigamiNbtManager.getNbt(user, shikigamiType);

        T shikigami = summonShikigami(user, serverWorld, shikigamiType, nbt, pos);
        if (shikigami == null) return null;

        NbtCompound newNbt = new NbtCompound();
        shikigami.saveNbt(newNbt);
        newNbt.putBoolean("summoned", true);

        ShikigamiNbtManager.setNbt(user, shikigamiType, newNbt);

        return shikigami;
    }
}
