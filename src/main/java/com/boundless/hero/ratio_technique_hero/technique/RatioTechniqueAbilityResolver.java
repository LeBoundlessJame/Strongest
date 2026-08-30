package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.mechanics.BlackFlashManager;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getRightClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static float resolveRatioDamage(PlayerEntity player, float baseDamage) {
        if (!BlackFlashManager.shouldBlackFlash(player)) return baseDamage;
        return BlackFlashManager.resolveBlackFlashDamage(player, baseDamage);
    }
}
