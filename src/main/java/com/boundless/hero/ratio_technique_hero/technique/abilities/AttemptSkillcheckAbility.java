package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.ratio_technique_hero.technique.RatioComponents;
import com.boundless.hero.ratio_technique_hero.technique.RatioSkillcheck;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AttemptSkillcheckAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        RatioSkillcheck skillcheck = HeroUtils.getHeroStack(player).get(RatioComponents.RATIO_SKILLCHECK);
        if (skillcheck == null) return;

        if (skillcheck.isSuccessful(player.getWorld().getTime())) {
            player.sendMessage(Text.of("Ratio success!"));
        } else {
            player.sendMessage(Text.of("Ratio fail."));
        }

        HeroUtils.getHeroStack(player).remove(RatioComponents.RATIO_SKILLCHECK);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("ratio_skillcheck");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Attempt skillcheck");
    }
}
