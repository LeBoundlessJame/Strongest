package com.boundless.mixin;

import com.boundless.util.HeroUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
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

    @Inject(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", at = @At("HEAD"), cancellable = true)
    private void updatePassengerPosition(Entity passenger, Entity.PositionUpdater updater, CallbackInfo ci) {
        Entity grabUser = (Entity) (Object) this;
        if (grabUser instanceof PlayerEntity player && HeroUtils.isHero(player)) {
            ci.cancel();
            Vec3d riderPos = repositionRider(grabUser);
            Vec3d attachment = passenger.getVehicleAttachmentPos(grabUser);
            updater.accept(passenger, riderPos.x - attachment.x, riderPos.y - attachment.y + 1.0, riderPos.z - attachment.z);
        }
    }

    @Unique
    private Vec3d repositionRider(Entity user) {
        Box box = user.getBoundingBox();
        double scale = box.getLengthX() + box.getLengthZ();

        Vec3d rotationVec = user.getRotationVec(1.0F).normalize().multiply(scale);
        return this.pos.add(rotationVec.x, 0.0D, rotationVec.z);
    }
}
