package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class FlightPoseMixin {
    @ModifyReturnValue(method = "isInSwimmingPose", at = @At(value = "RETURN"))
    protected boolean boundless$updatePose(boolean original) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity instanceof PlayerEntity player) {
            return original && HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_TICKS, 0) <= 0;
        }
        return original;
    }
}
