package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getRightClickAbility(PlayerEntity player) {
        return TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static Identifier getOvertimeAbility(PlayerEntity player) {
        long time = player.getWorld().getTimeOfDay() % 24000;
        boolean isWorkingHours = time >= 3000 && time <= 11000;
        return isWorkingHours ? TechniqueAbilityRegistry.EMPTY.getAbilityId() : RatioTechnique.OVERTIME.getAbilityId();
    }
}
