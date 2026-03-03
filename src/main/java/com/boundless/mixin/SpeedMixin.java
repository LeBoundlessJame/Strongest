package com.boundless.mixin;

import com.boundless.config.CombatConfig;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.HeroUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class SpeedMixin {
    private static CombatConfig COMBAT_CONFIG = ConfigRegistry.HERO_CONFIG.COMBAT_CONFIG;

    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    private float boundless$increaseSpeed(float original) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!HeroUtils.isHero(player) || !player.isSprinting()) return original;

        int elapsedSprintTicks = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.SPRINT_TICKS, 0);
        float topSpeedMultiplier = (float) player.getAttributeValue(AttributeRegistry.TOP_SPEED_MULTIPLIER);
        int ticksUntilMaxSpeed = (int) (player.getAttributeValue(AttributeRegistry.TIME_UNTIL_MAX_SPEED) * 20);

        float delta = (float) elapsedSprintTicks / ticksUntilMaxSpeed;
        float result = MathHelper.lerp(Math.clamp(delta, 0.0f, 1.0f), original, original * topSpeedMultiplier);

        if (player.hasStatusEffect(StatusEffectRegistry.LIMITED_SPEED)) {
            result = Math.min(result, COMBAT_CONFIG.maxCombatSprintSpeed.get());
        }

        return result;
    }
}