package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DivergentFistAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(YujiComponents.DIVERGENCE_ACTIVE, true);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("divergent_fist");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Divergent Fist");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 200;
    }
}
