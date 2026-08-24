package com.boundless.loadouts;

import net.minecraft.util.Identifier;

import java.util.Map;

public record TechniqueLoadoutComponent(Map<AbilityKey, Identifier> abilities) {}
