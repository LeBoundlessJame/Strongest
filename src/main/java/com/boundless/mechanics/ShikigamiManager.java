package com.boundless.mechanics;

import com.boundless.BoundlessAPI;
import com.boundless.hero.shadow_hero.ShadowHero;
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
        if (player.getWorld().isClient || !(player.getWorld() instanceof ServerWorld serverWorld)) return;

        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<String, NbtCompound> map = heroStack.getOrDefault(ShadowHero.SHIKIGAMI, new HashMap<>());
        HashMap<String, NbtCompound> clone = new HashMap<>(map);

        String shikigamiKey = shikigamiType.toString();
        NbtCompound nbt = map.getOrDefault(shikigamiKey, new NbtCompound());

        boolean nowSummoned = !nbt.getBoolean("summoned");
        nbt.putBoolean("summoned", nowSummoned);

        if (nowSummoned) {
            nbt.remove("UUID");

            Optional<Entity> entityOptional = EntityType.getEntityFromNbt(nbt, serverWorld);
            T newEntity = entityOptional.isPresent() ? summonAtRaycastBlock(player, (T) entityOptional.get()) : summonAtRaycastBlock(player, shikigamiType.create(serverWorld));

            if (newEntity == null) return;

            newEntity.setPersistent();
            newEntity.setTamed(true, false);
            newEntity.saveNbt(nbt);
        } else {
            UUID uuid = nbt.getUuid("UUID");
            Entity entity = serverWorld.getEntity(uuid);

            if (entity != null) {
                EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), entity, entity.getPos().add(0, 1, 0), new Vec3d(0.15, 0.15, 0.15));

                entity.saveNbt(nbt);
                entity.discard();
            }
        }

        clone.put(shikigamiKey, nbt);
        heroStack.set(ShadowHero.SHIKIGAMI, clone);
    }

    public static <T extends TameableEntity & Shikigami> T summonAtRaycastBlock(PlayerEntity player, T shikigami) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 16);
        if (blockHitResult == null) return null;

        BlockPos pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());

        shikigami.setPosition(pos.toCenterPos());
        shikigami.setOwner(player);
        player.getWorld().spawnEntity(shikigami);
        EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), shikigami, shikigami.getPos(), new Vec3d(0.15, 0.15, 0.15));

        return shikigami;
    }

    public static <T extends TameableEntity & Shikigami> T summonShikigami(PlayerEntity player, T shikigami) {
        shikigami.setPos(player.getX(), player.getY(), player.getZ());
        shikigami.setOwner(player);
        player.getWorld().spawnEntity(shikigami);
        return shikigami;
    }
}
