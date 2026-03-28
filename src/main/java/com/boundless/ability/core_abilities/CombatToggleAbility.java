package com.boundless.ability.core_abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;

public class CombatToggleAbility extends Ability {
    public CombatToggleAbility() {
        super(BoundlessAPI.identifier("combat_toggle"));
        this.setCooldown(5);
        this.setSkillSlot(2);
        this.setIcon(BoundlessAPI.hudPNG("open"));
    }

    @Override
    public void executeAbility(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        HeroUtils.getHeroStack(player).set(DataComponentRegistry.COMBAT_MODE_ENABLED, !HeroUtils.combatModeEnabled(player));
    }
}
