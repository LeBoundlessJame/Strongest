package com.boundless.mixin;

import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class SpeedMixin {
    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    private float boundless$increaseSpeed(float original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!HeroUtils.isHero(player)) return original;
        int elapsedSprintTicks = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.SPRINT_TICKS, 0);
        int timeUntilMaxSpeed = 40;
        float delta = (float) elapsedSprintTicks / timeUntilMaxSpeed;
        return MathHelper.lerp(Math.clamp(delta, 0.0f, 1.0f), original, original * 2.5f);
    }
}