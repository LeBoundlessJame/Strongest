package com.boundless.mechanics;

import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.util.HeroUtils;
import com.boundless.util.Shikigami;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class ShikigamiNbtManager {
    public static <T extends TameableEntity & Shikigami> NbtCompound getNbt(PlayerEntity player, EntityType<T> shikigamiType) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<String, NbtCompound> shikigamiMap = heroStack.getOrDefault(TenShadowsComponents.SHIKIGAMI, new HashMap<>());

        NbtCompound nbt = shikigamiMap.getOrDefault(shikigamiType.toString(), new NbtCompound());
        return nbt;
    }

    public static <T extends TameableEntity & Shikigami> void setNbt(PlayerEntity player, EntityType<T> shikigamiType, NbtCompound nbt) {
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        Map<String, NbtCompound> shikigamiMap = heroStack.getOrDefault(TenShadowsComponents.SHIKIGAMI, new HashMap<>());

        shikigamiMap = new HashMap<>(shikigamiMap);
        shikigamiMap.put(shikigamiType.toString(), nbt);
        heroStack.set(TenShadowsComponents.SHIKIGAMI, shikigamiMap);
    }
}
