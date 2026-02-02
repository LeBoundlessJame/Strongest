package com.boundless.mixin;

import com.boundless.ability.Grab;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class GrabMixin {
    @Shadow
    private Vec3d pos;

    private Vec3d customOffset = Vec3d.ZERO;

    public void updateCustomOffset(Entity grabUser) {
        float delta = 0.0f;

        if (grabUser instanceof PlayerEntity player) {
            float elapsed = player.getWorld().getTime() - HeroUtils.getHeroStack(player).getOrDefault(Grab.GRAB_START, 0L);
            float duration = HeroUtils.getHeroStack(player).getOrDefault(Grab.GRAB_END, 0L) - HeroUtils.getHeroStack(player).getOrDefault(Grab.GRAB_START, 0L);
            delta = Math.clamp(elapsed / duration, 0, 1);
        }
        this.customOffset = Grab.suplex(grabUser, new Vec3d(0, 0, 0), delta);
        //this.customOffset = new Vec3d(Math.cos(entity.age * 0.1) * 0.5, Math.sin(entity.age * 0.1) * 0.5, 0);
    }

    @Inject(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", at = @At("HEAD"), cancellable = true)
    private void updatePassengerPosition(Entity passenger, Entity.PositionUpdater updater, CallbackInfo ci) {
        Entity grabUser = (Entity) (Object) this;
        if (grabUser instanceof PlayerEntity player && HeroUtils.isHero(player)) {
            ci.cancel();
            Vec3d riderPos = repositionRider(grabUser);
            Vec3d attachment = passenger.getVehicleAttachmentPos(grabUser);
            updater.accept(passenger, riderPos.x - attachment.x, riderPos.y - attachment.y, riderPos.z - attachment.z);
        }
    }

    @Unique
    private Vec3d repositionRider(Entity grabUser) {
        Box box = grabUser.getBoundingBox();
        double scale = box.getLengthX() + box.getLengthZ();
        Vec3d rotationVec = grabUser.getRotationVec(1.0F).normalize().multiply(scale);
        updateCustomOffset(grabUser);
        return this.pos.add(rotationVec.x, 0.0f, rotationVec.z).add(this.customOffset);
    }
}
