package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.rock.RockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {
    public static final EntityType<HeroActionEntity> HERO_ACTION_ENTITY = registerEntityType("hero_action", HeroActionEntity::new, 1, 1);
    public static final EntityType<RockEntity> ROCK = registerEntityType("rock", RockEntity::new, 1, 1);

    public static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return Registry.register(Registries.ENTITY_TYPE, BoundlessAPI.identifier(name), EntityType.Builder.<T>create(factory, SpawnGroup.MISC).dimensions(width, height).build(name));
    }

    public static void initialize() {}
}
