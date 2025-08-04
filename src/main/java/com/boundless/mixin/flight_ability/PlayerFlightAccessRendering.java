package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.FlightRendering;
import com.boundless.util.FlightAccess;
import com.boundless.util.HeroUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerFlightAccessRendering extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> implements FlightAccess {
    @Unique
    private float currentFlightRotation = 0f;

    public PlayerFlightAccessRendering(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(at = @At(value = "HEAD"), method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", cancellable = true)
    protected void boundless$flightTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, CallbackInfo ci) {
        if (!abstractClientPlayerEntity.getAbilities().flying || !HeroUtils.isHero(abstractClientPlayerEntity)) return;
        super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, tickDelta, i);
        FlightRendering.hoverRendering(abstractClientPlayerEntity, matrixStack, f, g, tickDelta, i, (PlayerEntityRenderer)(Object)this, ci);
        ci.cancel();
    }

    @Override
    public float boundless$getFlightRotation() {
        return currentFlightRotation;
    }

    @Override
    public void boundless$setFlightRotation(float rotation) {
        this.currentFlightRotation = rotation;
    }

    @Override
    public void boundless$adjustFlightRotation(float rotationAdjustment, float min, float max) {
        float rotation = this.currentFlightRotation;
        rotation = Math.clamp(rotation + rotationAdjustment, min, max);
        this.currentFlightRotation = rotation;
    }
}