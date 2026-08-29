package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getRightClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        return RatioTechnique.PUNCH.getAbilityId();
    }

    //        if (HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK) != null) return RatioTechnique.ATTEMPT_SKILLCHECK.getAbilityId();

    public static Identifier getRatioAbility(PlayerEntity player) {
        return RatioTechnique.RATIO.getAbilityId();
    }
}
