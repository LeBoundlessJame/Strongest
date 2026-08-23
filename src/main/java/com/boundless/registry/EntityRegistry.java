package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.entity.divine_dogs.shiro.DivineDogShiroEntity;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.entity.open.OpenEntity;
import com.boundless.entity.rock.RockEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {
    public static final EntityType<HeroActionEntity> HERO_ACTION_ENTITY = registerEntityType("hero_action", HeroActionEntity::new, 1, 1);
    public static final EntityType<RockEntity> ROCK = registerEntityType("rock", RockEntity::new, 0.8f, 0.8f);
    public static final EntityType<OpenEntity> OPEN_ENTITY = registerEntityType("open", OpenEntity::new, 1, 1);
    public static final EntityType<MalevolentShrineEntity> MALEVOLENT_SHRINE = registerEntityType("malevolent_shrine", MalevolentShrineEntity::new, 8, 8);
    public static final EntityType<DivineDogKuroEntity> DIVINE_DOG_KURO = registerEntityType("divine_dog_kuro", DivineDogKuroEntity::new, 0.6F, 0.85F, 0.68F);
    public static final EntityType<DivineDogShiroEntity> DIVINE_DOG_SHIRO = registerEntityType("divine_dog_shiro", DivineDogShiroEntity::new, 0.6F, 0.85F, 0.68F);
    public static final EntityType<GamaEntity> GAMA = registerEntityType("gama", GamaEntity::new, 0.6F, 0.85F, 0.68F);
    public static final EntityType<GrappleEntity> GRAPPLE = registerEntityType("grapple", GrappleEntity::new, 0.6F, 0.85F, 0.68F);

    public static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.EntityFactory<T> factory, float width, float height) {
        return Registry.register(Registries.ENTITY_TYPE, BoundlessAPI.identifier(name), EntityType.Builder.<T>create(factory, SpawnGroup.MISC).dimensions(width, height).build(name));
    }

    public static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.EntityFactory<T> factory, float width, float height, float eyeHeight) {
        return Registry.register(Registries.ENTITY_TYPE, BoundlessAPI.identifier(name), EntityType.Builder.<T>create(factory, SpawnGroup.MISC).dimensions(width, height).eyeHeight(eyeHeight).build(name));
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(DIVINE_DOG_KURO, DivineDogKuroEntity.createWolfAttributes());
        FabricDefaultAttributeRegistry.register(DIVINE_DOG_SHIRO, DivineDogShiroEntity.createWolfAttributes());
        FabricDefaultAttributeRegistry.register(GAMA, GamaEntity.createFrogAttributes());
    }

    public static void initialize() {
        registerAttributes();
    }
}
