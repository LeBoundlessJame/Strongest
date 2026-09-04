package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class KickAbility extends PunchAbility {
    public KickAbility() {
        this.setImpactTick(9);
        this.setAttackDuration(17);
        this.setAnimation(BoundlessAPI.id("roundhouse"));
        this.setAnimationSpeed(1.15f);
        this.setKnockback(new Vec3d(1.2, 0.6, 1.2));
    }

    @Override
    public float getDamage(PlayerEntity player) {
        return super.getDamage(player) * 2;
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("roundhouse_kick");
    }
}
