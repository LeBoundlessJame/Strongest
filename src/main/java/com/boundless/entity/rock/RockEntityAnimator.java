package com.boundless.entity.rock;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class RockEntityAnimator extends AzEntityAnimator<RockEntity> {
    private static final Identifier ANIMATIONS = Identifier.of(BoundlessAPI.MOD_ID,
            "animations/entity/rock.animation.json");

    @Override
    public void registerControllers(AzAnimationControllerContainer<RockEntity> animationControllerContainer) {
        animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
    }

    @Override
    public @NotNull Identifier getAnimationLocation(RockEntity animatable) {
        return ANIMATIONS;
    }
}
