package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.generic.MenuToggleAbility;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SelectTargetMenu extends MenuToggleAbility {
    @Override
    public Text getDisplayText(LivingEntity user) {
        return ComponentUtils.getOr(user, StrongestComponents.MENU_TOGGLE,false) ? Text.of("Target selection ON") : Text.of("Target selection OFF");
    }
}
