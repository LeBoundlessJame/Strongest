package com.boundless.mechanics;

import com.boundless.BoundlessAPI;
import com.boundless.hero.shadow_hero.ShadowHero;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import com.boundless.util.Shikigami;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ShikigamiManager {
    public static <T extends TameableEntity & Shikigami> void toggleShikigami(PlayerEntity player, EntityType<T> shikigamiType) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return;

        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<String, NbtCompound> shikigamiMap = heroStack.getOrDefault(TenShadowsComponents.SHIKIGAMI, new HashMap<>());

        String shikigamiKey = shikigamiType.toString();
        NbtCompound nbt = shikigamiMap.getOrDefault(shikigamiKey, new NbtCompound());

        NbtCompound result = nbt.getBoolean("summoned")
                ? desummonShikigami(serverWorld, nbt)
                : summonShikigami(player, serverWorld, shikigamiType, nbt);

        if (result == null) return;

        shikigamiMap = new HashMap<>(shikigamiMap);
        shikigamiMap.put(shikigamiKey, result);
        heroStack.set(TenShadowsComponents.SHIKIGAMI, shikigamiMap);
    }

    // I'm not a big fan of using nbtCompound as the return value, but nbt is a bit cooked so rip
    // Todo: also, might make range configurable in the future or extract it back out to a generic helper
    private static <T extends TameableEntity & Shikigami> NbtCompound summonShikigami(PlayerEntity playerEntity, ServerWorld serverWorld, EntityType<T> shikigamiType, NbtCompound nbt) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(playerEntity, 16);
        if (blockHitResult == null) return null;
        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

        T shikigami;

        if (nbt.contains("UUID")) {
            Optional<Entity> entity = EntityType.getEntityFromNbt(nbt, serverWorld);
            if (entity.isEmpty()) return null;

            shikigami = (T) entity.get();
            shikigami.setPosition(pos.toCenterPos());
            serverWorld.spawnEntity(shikigami);
        } else {
            shikigami = shikigamiType.create(serverWorld);
            if (shikigami == null) return null;

            shikigami.setPosition(pos.toCenterPos());
            shikigami.setOwner(playerEntity);
            serverWorld.spawnEntity(shikigami);

            shikigami.setPersistent();
            shikigami.setTamed(true, false);
        }

        EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), shikigami, shikigami.getPos(), new Vec3d(0.15, 0.15, 0.15));

        NbtCompound newNbt = new NbtCompound();
        shikigami.saveNbt(newNbt);
        newNbt.putBoolean("summoned", true);

        return newNbt;
    }

    private static NbtCompound desummonShikigami(ServerWorld serverWorld, NbtCompound nbt) {
        if (!nbt.contains("UUID")) return null;

        UUID uuid = nbt.getUuid("UUID");
        Entity entity = serverWorld.getEntity(uuid);

        if (entity == null) {
            nbt.putBoolean("summoned", false);
            return nbt;
        }

        NbtCompound newNbt = new NbtCompound();
        entity.saveNbt(newNbt);

        EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), entity, entity.getPos().add(0, 1, 0), new Vec3d(0.15, 0.15, 0.15));

        entity.discard();
        newNbt.putBoolean("summoned", false);

        return newNbt;
    }
}
