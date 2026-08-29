package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.mechanics.CooldownManager;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RatioAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK);

        if (skillcheck == null) {
            RatioAbility.startSkillcheck(player, 40, 20, 2);
        } else {
            player.sendMessage(Text.of(skillcheck.isSuccessful(player.getWorld().getTime()) ? "Ratio succcess!" : "Ratio fail"));
            HeroUtils.getHeroStack(player).remove(RatioComponents.RATIO_SKILLCHECK);
            CooldownManager.setAbilityCooldownIfHigher(player, this.getAbilityId(), 200);
        }
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("ratio");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 5;
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 233;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Ratio");
    }

    private static void startSkillcheck(PlayerEntity player, long duration, long ticksUntilTarget, long leniency) {
        long startTick = player.getWorld().getTime();

        RatioSkillcheck skillcheck = new RatioSkillcheck(startTick, startTick + duration, startTick + ticksUntilTarget, leniency);
        HeroUtils.getHeroStack(player).set(RatioComponents.RATIO_SKILLCHECK, skillcheck);
    }
}
