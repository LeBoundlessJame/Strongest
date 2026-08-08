package com.boundless.util;

import com.boundless.hero.shadow_hero.ShadowHero;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ShikigamiUtils {
    public static <T extends TameableEntity & Shikigami> void toggleShikigami(PlayerEntity player, T shikigami) {
        if (player.getWorld().isClient || !(player.getWorld() instanceof ServerWorld serverWorld)) return;

        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<String, NbtCompound> map = heroStack.getOrDefault(ShadowHero.SHIKIGAMI, new HashMap<>());
        HashMap<String, NbtCompound> clone = new HashMap<>(map);

        String shikigamiKey = shikigami.getType().toString();
        NbtCompound nbt = map.getOrDefault(shikigamiKey, new NbtCompound());

        boolean nowSummoned = !nbt.getBoolean("summoned");
        nbt.putBoolean("summoned", nowSummoned);

        if (nowSummoned) {
            nbt.remove("UUID");

            Optional<Entity> entityOptional = EntityType.getEntityFromNbt(nbt, serverWorld);
            T newEntity = entityOptional.isPresent() ? summonShikigami(player, (T) entityOptional.get()) : summonShikigami(player, shikigami);

            newEntity.setPersistent();
            newEntity.setTamed(true, false);
            newEntity.saveNbt(nbt);
        } else {
            UUID uuid = nbt.getUuid("UUID");
            Entity entity = serverWorld.getEntity(uuid);
            if (entity != null) {
                entity.saveNbt(nbt);
                entity.discard();
            }
        }

        clone.put(shikigamiKey, nbt);
        heroStack.set(ShadowHero.SHIKIGAMI, clone);
    }

    public static <T extends TameableEntity & Shikigami> T summonShikigami(PlayerEntity player, T shikigami) {
        shikigami.setPos(player.getX(), player.getY(), player.getZ());
        shikigami.setOwner(player);
        player.getWorld().spawnEntity(shikigami);
        return shikigami;
    }
}
