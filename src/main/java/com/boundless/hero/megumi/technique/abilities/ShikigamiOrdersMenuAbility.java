package com.boundless.hero.megumi.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.megumi.technique.TenShadowsComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ShikigamiOrdersMenuAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ComponentUtils.toggleBoolean(player, TenShadowsComponents.SHIKIGAMI_ORDER_MENU,false);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("shikigami_orders_menu");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.literal("Shikigami Orders");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 5;
    }
}
