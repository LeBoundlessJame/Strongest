package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getRightClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.PUNCH.getAbilityId();
    }
}
