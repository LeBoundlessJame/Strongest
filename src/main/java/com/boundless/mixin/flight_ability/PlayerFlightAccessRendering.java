package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.FlightRendering;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerFlightAccessRendering extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerFlightAccessRendering(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    // Todo: fix this soon, not a good idea to blindly cancel
    @Inject(at = @At(value = "HEAD"), method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", cancellable = true)
    protected void boundless$flightTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float tickDelta, float i, CallbackInfo ci) {
        super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, tickDelta, i);
        FlightRendering.renderFlight(abstractClientPlayerEntity, matrixStack, f, g, tickDelta, i, (PlayerEntityRenderer) (Object) this);
        ci.cancel();
    }
}