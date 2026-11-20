package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ManjiKickAbility extends KickAbility {
    public ManjiKickAbility() {
        this.setAnimation(BoundlessAPI.id("manji"));
        this.setKnockback(new Vec3d(1.4, 1.2f, 1.4));
        this.setAttackDuration(20);
        this.setImpactTick(11);
        this.setAnimationSpeed(1.0f);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("manji_kick");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 100;
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Manji Kick");
    }

    @Override
    public boolean canActivate(LivingEntity user) {
        return super.canActivate(user) && user.isOnGround();
    }
}
