package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.RatioAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class RatioTechnique {
    public static final TechniqueAbility RATIO = TechniqueAbilityRegistry.register(new RatioAbility());

    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(PunchAbility.builder()
            .abilityId(BoundlessAPI.id("nanami_punch"))
            .damage(22)
            .attackDuration(8)
            .whiffSound(SoundRegistry.MISS_HIT)
            .animation(BoundlessAPI.id("nanami_slash"))
            .mirrorAnimationProvider((playerEntity -> false))
            .build());

    public static void ratioTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        if (skillcheck.isExpired(player.getWorld().getTime()) || !(skillcheck.canStillSucceed(player.getWorld().getTime()))) {
            HeroUtils.getHeroStack(player).remove(RatioComponents.RATIO_SKILLCHECK);
        }
    }
}
