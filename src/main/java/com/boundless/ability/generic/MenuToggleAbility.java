package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MenuToggleAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        ComponentUtils.toggleBoolean(player, StrongestComponents.MENU_TOGGLE,false);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("menu_toggle");
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return ComponentUtils.getOr(playerEntity, StrongestComponents.MENU_TOGGLE,false) ? Text.of("Open menu") : Text.of("Close menu");
    }
}
