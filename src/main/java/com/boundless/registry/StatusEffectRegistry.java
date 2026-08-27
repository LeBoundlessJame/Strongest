package com.boundless.registry;

import com.boundless.BoundlessAPI;
import com.boundless.effect.BleedEffect;
import com.boundless.effect.ImpactFrameEffect;
import com.boundless.effect.InvulnerabilityEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class StatusEffectRegistry {
    public static final RegistryEntry<StatusEffect> INVULNERABILITY_EFFECT = registerStatusEffect("invulnerability", new InvulnerabilityEffect(StatusEffectCategory.BENEFICIAL, 0x5d8385));
    public static final RegistryEntry<StatusEffect> IMPACT_FRAME_EFFECT = registerStatusEffect("impact_frame_effect", new ImpactFrameEffect(StatusEffectCategory.BENEFICIAL, 0x5d8385));
    public static final RegistryEntry<StatusEffect> CLAP_IMPACT_FRAME_EFFECT = registerStatusEffect("clap_impact_frame_effect", new ImpactFrameEffect(StatusEffectCategory.BENEFICIAL, 0x5d8385));
    public static final RegistryEntry<StatusEffect> BONE_BREAK_EFFECT = registerStatusEffect("bone_break_effect", new ImpactFrameEffect(StatusEffectCategory.HARMFUL, 0xffffff));
    public static final RegistryEntry<StatusEffect> CINEMATIC_BARS = registerStatusEffect("cinematic_bars_effect", new ImpactFrameEffect(StatusEffectCategory.NEUTRAL, 0xffffff));
    public static final RegistryEntry<StatusEffect> SHRINE_EFFECT = registerStatusEffect("shrine_effect", new ImpactFrameEffect(StatusEffectCategory.BENEFICIAL, 0x5d8385));
    public static final RegistryEntry<StatusEffect> GRAYSCALE = registerStatusEffect("grayscale_effect", new ImpactFrameEffect(StatusEffectCategory.NEUTRAL, 0xffffff));
    public static final RegistryEntry<StatusEffect> BLEED = registerStatusEffect("bleed", new BleedEffect(StatusEffectCategory.HARMFUL, 0xff0000));

    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, BoundlessAPI.id(name), statusEffect);
    }

    public static void initialize() {
    }
}
