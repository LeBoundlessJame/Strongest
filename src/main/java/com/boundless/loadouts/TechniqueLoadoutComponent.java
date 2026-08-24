package com.boundless.loadouts;


import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.Map;

public record TechniqueLoadoutComponent(Map<AbilityKey, Identifier> abilities) {
    public static final Codec<TechniqueLoadoutComponent> CODEC = Codec.unboundedMap(AbilityKey.CODEC, Identifier.CODEC)
            .xmap(TechniqueLoadoutComponent::new, TechniqueLoadoutComponent::abilities);
}
