package com.boundless.loadouts;


import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record TechniqueLoadoutComponent(Map<AbilityKey, Set<Identifier>> abilities) {
    private static final Codec<Set<Identifier>> ABILITY_SET_CODEC = Identifier.CODEC.listOf().xmap(Set::copyOf, List::copyOf);

    public static final Codec<TechniqueLoadoutComponent> CODEC = Codec.unboundedMap(AbilityKey.CODEC, ABILITY_SET_CODEC)
            .xmap(TechniqueLoadoutComponent::new, TechniqueLoadoutComponent::abilities);
}
