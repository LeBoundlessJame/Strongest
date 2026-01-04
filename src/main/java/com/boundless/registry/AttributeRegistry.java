package com.boundless.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class AttributeRegistry {
    public static final RegistryEntry<EntityAttribute> DAMAGE_RESISTANCE = registerAttribute("damage_resistance", new ClampedEntityAttribute("boundless.damage_resistance", 1.0D, 1.0D, 2).setTracked(true));
    public static final RegistryEntry<EntityAttribute> TOP_SPEED_MULTIPLIER = registerAttribute("top_speed_multiplier", new ClampedEntityAttribute("boundless.top_speed_multiplier", 1.0D, -999.0D, 999.0D).setTracked(true));
    public static final RegistryEntry<EntityAttribute> TIME_UNTIL_MAX_SPEED = registerAttribute("time_until_max_speed", new ClampedEntityAttribute("boundless.ticks_until_max_speed", 0.0D, 0.0D, 999.0D).setTracked(true));

    private static RegistryEntry<EntityAttribute> registerAttribute(String name, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, BoundlessAPI.identifier(name), attribute);
    }

    public static void initialize() {}
}
