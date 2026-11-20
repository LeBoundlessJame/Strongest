package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MenuToggleAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (user.getWorld().isClient) return;
        ComponentUtils.toggleBoolean(user, StrongestComponents.MENU_TOGGLE,false);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("menu_toggle");
    }

    public Text getDisplayText(LivingEntity user) {
        return ComponentUtils.getOr(user, StrongestComponents.MENU_TOGGLE,false) ? Text.of("Open menu") : Text.of("Close menu");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 5;
    }
}
