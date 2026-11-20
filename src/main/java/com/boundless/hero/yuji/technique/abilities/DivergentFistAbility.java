package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DivergentFistAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        HeroUtils.getHeroStack(user).set(YujiComponents.DIVERGENCE_ACTIVE, true);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("divergent_fist");
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Divergent Fist");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 200;
    }
}
