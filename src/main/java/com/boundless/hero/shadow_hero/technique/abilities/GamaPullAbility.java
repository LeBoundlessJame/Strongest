package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class GamaPullAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        EntityHitResult result = RaycastUtils.raycast(player, 32);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 32, 1.5f) : result.getEntity();
        if (target == null || target == player) return;

        Vec3d direction = player.getPos().subtract(target.getPos()).normalize();
        double pullStrength = target.distanceTo(player) * 0.15;

        target.setVelocity(direction.add(0, 0.5, 0).multiply(pullStrength, 1, pullStrength));
        target.velocityModified = true;
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("gama_pull");
    }
}
