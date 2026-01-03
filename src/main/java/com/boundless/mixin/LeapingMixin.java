package com.boundless.mixin;

import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public abstract class LeapingMixin {
    @Shadow
    protected abstract float getJumpVelocity();

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"), method = "jump")
    public void boundless$jump(Args args) {
        if (!((LivingEntity) (Object) this instanceof PlayerEntity player)) return;
        if (!HeroUtils.isHero(player)) return;
        args.set(0, (double) args.get(0) * getJumpVelocity() * 4);
        args.set(2, (double) args.get(2) * getJumpVelocity() * 4);
    }
}
