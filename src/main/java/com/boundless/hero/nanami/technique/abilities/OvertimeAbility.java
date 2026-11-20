package com.boundless.hero.nanami.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.util.AttackUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class OvertimeAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        AttackUtils.startAttackTimer(user, 77);
        HeroUtils.getHeroStack(user).set(RatioComponents.OVERTIME_ELAPSED, 1);

        // TODO: I need to come up with a fix for this IMMEDIATELY
        //PlayerAnimationUtils.playSyncedAnimation(user, BoundlessAPI.id("overtime"));
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("overtime");
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.literal("Overtime").formatted(Formatting.AQUA).formatted(Formatting.BOLD);
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 12000;
    }

    @Override
    public boolean canActivate(LivingEntity user) {
        long time = user.getWorld().getTimeOfDay() % 24000;
        boolean isWorkingHours = time >= 3000 && time <= 11000;
        return super.canActivate(user) && !isWorkingHours;
    }
}
