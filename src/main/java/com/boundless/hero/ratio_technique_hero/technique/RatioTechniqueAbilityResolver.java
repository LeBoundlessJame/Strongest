package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getRightClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        if (HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK) != null) return RatioTechnique.ATTEMPT_SKILLCHECK.getAbilityId();
        return RatioTechnique.PUNCH.getAbilityId();
    }
}
