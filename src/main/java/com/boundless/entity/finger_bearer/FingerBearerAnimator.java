package com.boundless.entity.finger_bearer;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class FingerBearerAnimator extends AzEntityAnimator {
    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
    }

    @Override
    public @NotNull Identifier getAnimationLocation(Object o) {
        return BoundlessAPI.id("animations/entity/finger_bearer.animation.json");
    }
}