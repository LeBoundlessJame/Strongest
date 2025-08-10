package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class UpdateFlightPoseMixin {
    @ModifyExpressionValue(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isFallFlying()Z"))
    protected boolean boundless$updatePose(boolean original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        return original || HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_TICKS, 0) > 0;
    }
}