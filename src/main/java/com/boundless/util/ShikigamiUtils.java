package com.boundless.util;

import com.boundless.hero.shadow_hero.ShadowHero;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShikigamiUtils {
    public static <T extends TameableEntity & Shikigami> void toggleShikigami(PlayerEntity player, T shikigami) {
        if (player.getWorld().isClient || !(player.getWorld() instanceof ServerWorld serverWorld)) return;

        Map<String, NbtCompound> map = HeroUtils.getHeroStack(player).getOrDefault(ShadowHero.SHIKIGAMI, new HashMap<>());
        HashMap<String, NbtCompound> clone = new HashMap<>(map);
        NbtCompound nbt = map.getOrDefault(shikigami.getType().toString(), new NbtCompound().putBoolean("summoned", false));

        if (!nbt.getBoolean("summoned")) {
            Optional<Entity> entityOptional = EntityType.getEntityFromNbt(nbt, serverWorld);

            if (entityOptional.isPresent()) {
                summonShikigami(player, (T) entityOptional.get());
            } else {
                T newEntity = summonShikigami(player, shikigami);
                newEntity.writeNbt(nbt);
                System.out.println("Writing nbt");
            }
        } else {
            Optional<Entity> entityOptional = EntityType.getEntityFromNbt(nbt, serverWorld);
            if (entityOptional.isPresent()) {
                Entity entity = entityOptional.get();
                entity.writeNbt(nbt);
                entity.discard();
                System.out.println("Aw hell naw mf just got discarded");
            }
        }

        nbt.putBoolean("summoned", !nbt.getBoolean("summoned"));

        clone.put(shikigami.getType().toString(), nbt);
        HeroUtils.getHeroStack(player).set(ShadowHero.SHIKIGAMI, clone);
    }

    public static <T extends TameableEntity & Shikigami> T summonShikigami(PlayerEntity player, T shikigami) {
        shikigami.setPos(player.getPos().getX(), player.getPos().getY(), player.getPos().getZ());
        shikigami.setOwner(player);
        player.getWorld().spawnEntity(shikigami);
        return shikigami;
    }

    /*
    // Todo: extracting this out to a generic summon method soon
    public static void summonDivineDogKuro(PlayerEntity player, DivineDogKuroEntity shikigami) {
        if (player.getWorld().isClient) return;
        Map<String, NbtCompound> shikigamiMap = HeroUtils.getHeroStack(player).getOrDefault(ShadowHero.SHIKIGAMI, new HashMap<>());
        if (shikigamiMap.get(shikigami.getName().getString()) == null) {
            DivineDogKuroEntity entity = summonOnTopBlock(player, shikigami);
            if (entity != null) {
                NbtCompound data = new NbtCompound();

                entity.writeCustomDataToNbt(data);
                data.putUuid("uuid", entity.getUuid());

                shikigamiMap.put(shikigami.getName().getString(), data);
                HeroUtils.getHeroStack(player).set(ShadowHero.SHIKIGAMI, shikigamiMap);
                System.out.println("Summoned new shikigami");
            }
        } else {
            System.out.println(shikigamiMap.get(shikigami.getName().getString()).getUuid("uuid"));
            System.out.println("Desummoned shikigami");
        }
    }

    public static DivineDogKuroEntity summonOnTopBlock(PlayerEntity player, DivineDogKuroEntity shikigami) {
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 16);
        if (blockHitResult == null) return null;

        BlockPos pos = blockHitResult.getBlockPos();

        for (int i = 0; i < 64; i++) {
            if (player.getWorld().getBlockState(pos.up(i)).isAir()) {
                pos = pos.up();
                break;
            }
        }

        shikigami.setPos(pos.getX(), pos.getY(), pos.getZ());
        shikigami.setOwner(player);
        player.getWorld().spawnEntity(shikigami);
        return shikigami;
    }

     */
}
