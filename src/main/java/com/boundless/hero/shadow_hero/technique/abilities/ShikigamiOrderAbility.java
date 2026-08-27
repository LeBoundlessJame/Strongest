package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ShikigamiOrderAbility extends TechniqueAbility {

    @Override
    public void activate(PlayerEntity player) {

    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("shikigami_order");
    }

    @Override
    public String getDisplayString() {
        return "Shikigami Order";
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 2;
    }
}
