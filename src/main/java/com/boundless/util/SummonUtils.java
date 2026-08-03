package com.boundless.util;

import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.hero.shadow_hero.ShadowHero;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class SummonUtils {

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
}
