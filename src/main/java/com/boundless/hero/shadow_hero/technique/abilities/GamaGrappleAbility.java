package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

public class GamaGrappleAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!(player.getControllingVehicle() instanceof GamaEntity gama)) return;

        GrappleEntity grapple = gama.getGrapple();

        if (grapple == null) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 128);
            if (blockHitResult == null) return;

            grapple = new GrappleEntity(gama, player.getWorld());
            grapple.setVelocity(0, 0, 0);
            grapple.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grapple.setNoGravity(true);
            player.getWorld().spawnEntity(grapple);

            gama.setGrapple(grapple);
        } else {
            grapple.discard();
            gama.setGrapple(null);
        }
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("gama_grapple");
    }

    @Override
    public long getCooldown() {
        return 2;
    }

    @Override
    public boolean canActivate(PlayerEntity player) {
        return super.canActivate(player) &&
                player.getControllingVehicle() instanceof GamaEntity gama && gama.getOwner() == player;
    }

    @Override
    public String getDisplayString() {
        return "Gama Grapple";
    }
}
