
package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.generic.KickAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class DropkickAbility extends KickAbility {
    public DropkickAbility() {
        this.setAnimation(BoundlessAPI.id("dropkick"));
        this.setKnockback(new Vec3d(2.8, 0.6, 2.8));
        this.setAttackDuration(20);
        this.setImpactTick(5);
        this.setAnimationSpeed(2.0f);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("dropkick");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 20;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Dropkick");
    }
}
