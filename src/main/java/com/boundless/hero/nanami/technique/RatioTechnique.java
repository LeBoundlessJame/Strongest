package com.boundless.hero.nanami.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.hero.nanami.technique.abilities.CollapseAbility;
import com.boundless.hero.nanami.technique.abilities.OvertimeAbility;
import com.boundless.hero.nanami.technique.abilities.RatioAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class RatioTechnique {
    public static final int MAX_OVERTIME_DURATION = 6000;

    public static final TechniqueAbility RATIO = TechniqueAbilityRegistry.register(new RatioAbility());
    public static final TechniqueAbility COLLAPSE = TechniqueAbilityRegistry.register(new CollapseAbility().setDamage(30).setImpactTick(5).setRadius(new Vec3d(6, 5, 6)));
    public static final TechniqueAbility OVERTIME = TechniqueAbilityRegistry.register(new OvertimeAbility());

    public static void ratioTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        skillcheck(player);
        overtimeTick(player);
    }

    public static void overtimeTick(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);

        int elapsedOvertime = stack.getOrDefault(RatioComponents.OVERTIME_ELAPSED, 0);
        if (elapsedOvertime <= 0) return;
        if (elapsedOvertime >= MAX_OVERTIME_DURATION) {
            stack.set(RatioComponents.OVERTIME_ELAPSED, 0);
            return;
        }
        stack.set(RatioComponents.OVERTIME_ELAPSED, elapsedOvertime + 1);
    }

    public static void skillcheck(PlayerEntity player) {
        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        if (skillcheck.isExpired(player.getWorld().getTime()) || !(skillcheck.canStillSucceed(player.getWorld().getTime()))) {
            HeroUtils.getHeroStack(player).remove(RatioComponents.RATIO_SKILLCHECK);
        }
    }
}
