package com.boundless.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.BiConsumer;

public class AOEUtils {
    public static void forEach(Entity origin, float radius, BiConsumer<Entity, Entity> logic) {
        for (LivingEntity target : origin.getWorld().getEntitiesByClass(LivingEntity.class, origin.getBoundingBox().expand(radius), entity -> true)) {
            if (target != origin) logic.accept(origin, target);
        }
    }
}
