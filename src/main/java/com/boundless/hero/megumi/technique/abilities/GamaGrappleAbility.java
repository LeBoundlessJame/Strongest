package com.boundless.hero.megumi.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.entity.grapple.GrappleEntity;
import com.boundless.util.RaycastUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

public class GamaGrappleAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (user.getWorld().isClient) return;
        if (!(user.getControllingVehicle() instanceof GamaEntity gama)) return;

        GrappleEntity grapple = gama.getGrapple();

        if (grapple == null) {
            BlockHitResult blockHitResult = RaycastUtils.blockRaycast(user, 128);
            if (blockHitResult == null) return;

            grapple = new GrappleEntity(gama, user.getWorld());
            grapple.setVelocity(0, 0, 0);
            grapple.setPosition(Vec3d.of(blockHitResult.getBlockPos()));
            grapple.setNoGravity(true);
            user.getWorld().spawnEntity(grapple);

            gama.setGrapple(grapple);
        } else {
            grapple.discard();
            gama.setGrapple(null);
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("gama_grapple");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 2;
    }

    @Override
    public boolean canActivate(LivingEntity user) {
        return super.canActivate(user) &&
                user.getControllingVehicle() instanceof GamaEntity gama && gama.getOwner() == user;
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.literal("Gama Grapple");
    }
}
