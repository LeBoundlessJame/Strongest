package com.boundless.hero.ratio_technique_hero.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.CollapseAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.OvertimeAbility;
import com.boundless.hero.ratio_technique_hero.technique.abilities.RatioAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class RatioTechnique {
    public static final TechniqueAbility RATIO = TechniqueAbilityRegistry.register(new RatioAbility());
    public static final TechniqueAbility COLLAPSE = TechniqueAbilityRegistry.register(new CollapseAbility().setDamage(30).setImpactTick(0).setRadius(new Vec3d(4, 3, 4)));
    public static final TechniqueAbility OVERTIME = TechniqueAbilityRegistry.register(new OvertimeAbility());

    public static final TechniqueAbility PUNCH = TechniqueAbilityRegistry.register(new PunchAbility()
            .setAbilityId(BoundlessAPI.id("nanami_punch"))
            .setDamage(22)
            .setAttackDuration(10)
            .setWhiffSound(SoundRegistry.MISS_HIT));

    public static void ratioTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        if (skillcheck.isExpired(player.getWorld().getTime()) || !(skillcheck.canStillSucceed(player.getWorld().getTime()))) {
            HeroUtils.getHeroStack(player).remove(RatioComponents.RATIO_SKILLCHECK);
        }
    }
}
