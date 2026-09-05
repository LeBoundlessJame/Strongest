package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ManjiKickAbility extends KickAbility {
    public ManjiKickAbility() {
        this.setAnimation(BoundlessAPI.id("manji"));
        this.setKnockback(new Vec3d(2.8, 1.2f, 2.8));
        this.setAttackDuration(20);
        this.setImpactTick(8);
        this.setAnimationSpeed(1.0f);
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

    @Override
    public boolean canActivate(PlayerEntity player) {
        return super.canActivate(player) && player.isOnGround();
    }
}
