package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ShikigamiOrdersAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        DataComponentUtils.toggleBoolean(player, TenShadowsComponents.SHIKIGAMI_ORDER_MODE,false);
        System.out.println(HeroUtils.getHeroStack(player).get(TenShadowsComponents.SHIKIGAMI_ORDER_MODE));
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("shikigami_orders");
    }

    @Override
    public String getDisplayString() {
        return "Shikigami Orders";
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 2;
    }
}
