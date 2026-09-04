package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ManjiKickAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("manji_kick_parry"), 2.0f, false, true, 3000);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("manji_kick");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 20;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Manji Kick");
    }
}
