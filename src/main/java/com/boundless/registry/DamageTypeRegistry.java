package com.boundless.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypeRegistry {
    public static final RegistryKey<DamageType> BYPASS_DEFENCE = registerDamageType("bypass_defence");

    public static RegistryKey<DamageType> registerDamageType(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BoundlessAPI.identifier(name));
    }

    public static DamageSource getDamageSource(LivingEntity livingEntity, RegistryKey<DamageType> type) {
        return new DamageSource(livingEntity.getWorld().getRegistryManager().createRegistryLookup().getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(type));
    }

    public static void initialize() {
    }
}
