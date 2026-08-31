package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.util.HeroUtils;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class OvertimeAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(RatioComponents.OVERTIME_ELAPSED, 1);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("overtime"));
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("overtime");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.literal("Overtime").formatted(Formatting.AQUA).formatted(Formatting.BOLD);
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 12000;
    }

    @Override
    public boolean canActivate(PlayerEntity player) {
        long time = player.getWorld().getTimeOfDay() % 24000;
        boolean isWorkingHours = time >= 3000 && time <= 11000;
        return super.canActivate(player) && !isWorkingHours;
    }
}
