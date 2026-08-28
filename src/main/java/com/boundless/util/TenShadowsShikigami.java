package com.boundless.util;

import com.boundless.BoundlessAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public interface TenShadowsShikigami extends Shikigami {
    @Override
    default void onSummon(PlayerEntity player) {
        Entity shikigami = (Entity) this;
        EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), shikigami, shikigami.getPos().add(0, 1, 0), new Vec3d(0.15, 0.15, 0.15));
    }

    @Override
    default void onDesummon() {
        Entity shikigami = (Entity) this;
        EffekUtils.playEffect(BoundlessAPI.id("divine_dog_summon"), shikigami, shikigami.getPos().add(0, 1, 0), new Vec3d(0.15, 0.15, 0.15));
    }
}
