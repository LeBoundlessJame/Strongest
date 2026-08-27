package com.boundless.entity.gama;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class GamaAnimator<T extends AnimalEntity> extends AzEntityAnimator<T> {
    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
    }


    @Override
    public @NotNull Identifier getAnimationLocation(T animatable) {
        return BoundlessAPI.id("animations/entity/gama.animation.json");
    }
}
