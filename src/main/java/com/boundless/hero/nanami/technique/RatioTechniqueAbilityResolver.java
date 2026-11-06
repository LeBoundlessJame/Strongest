package com.boundless.hero.nanami.technique;

import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class RatioTechniqueAbilityResolver {
    public static Identifier getOvertimeAbility(PlayerEntity player) {
        long time = player.getWorld().getTimeOfDay() % 24000;
        boolean isWorkingHours = time >= 3000 && time <= 11000;
        return isWorkingHours ? TechniqueAbilityRegistry.EMPTY.getId() : RatioTechnique.OVERTIME.getId();
    }
}
