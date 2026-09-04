package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ManjiKickAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("manji_kick_indicator"));
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("manji_kick");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 20;
    }
}
