package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RatioAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        RatioAbility.startSkillcheck(player, 40, 20, 5);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("ratio");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 100;
    }

    private static void startSkillcheck(PlayerEntity player, long duration, long ticksUntilTarget, long leniency) {
        long startTick = player.getWorld().getTime();

        RatioSkillcheck skillcheck = new RatioSkillcheck(startTick, startTick + duration, startTick + ticksUntilTarget, leniency);
        HeroUtils.getHeroStack(player).set(RatioComponents.RATIO_SKILLCHECK, skillcheck);
    }
}
