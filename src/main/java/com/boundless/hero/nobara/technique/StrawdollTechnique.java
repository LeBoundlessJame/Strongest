package com.boundless.hero.nobara.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.nobara.technique.abilities.HairpinAbility;
import com.boundless.hero.nobara.technique.abilities.ResonanceAbility;
import com.boundless.hero.nobara.technique.abilities.ThrowNailAbility;
import com.boundless.hero.sukuna.technique.abilities.DismantleAbility;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class StrawdollTechnique {
    public static final TechniqueAbility HAIRPIN = TechniqueAbilityRegistry.register(new HairpinAbility());
    public static final TechniqueAbility RESONANCE = TechniqueAbilityRegistry.register(new ResonanceAbility());
    public static final TechniqueAbility THROW_NAIL = TechniqueAbilityRegistry.register(new ThrowNailAbility());
}