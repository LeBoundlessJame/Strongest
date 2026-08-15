package com.boundless.entity.divine_dogs.kuro;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class DivineDogAnimator<T extends WolfEntity> extends AzEntityAnimator<T> {
    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
    }


    @Override
    public @NotNull Identifier getAnimationLocation(T animatable) {
        return BoundlessAPI.identifier("animations/entity/divine_dog.animation.json");
    }
}
