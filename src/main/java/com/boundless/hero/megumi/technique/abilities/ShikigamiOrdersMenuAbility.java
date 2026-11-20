package com.boundless.hero.megumi.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.megumi.technique.TenShadowsComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ShikigamiOrdersMenuAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (user.getWorld().isClient) return;
        ComponentUtils.toggleBoolean(user, TenShadowsComponents.SHIKIGAMI_ORDER_MENU,false);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("shikigami_orders_menu");
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.literal("Shikigami Orders");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 5;
    }
}
