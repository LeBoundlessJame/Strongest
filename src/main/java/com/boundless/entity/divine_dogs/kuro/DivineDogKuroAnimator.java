package com.boundless.entity.divine_dogs.kuro;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.animation.controller.AzAnimationController;
import mod.azure.azurelib.common.animation.controller.AzAnimationControllerContainer;
import mod.azure.azurelib.common.animation.impl.AzEntityAnimator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class DivineDogKuroAnimator extends AzEntityAnimator<DivineDogKuroEntity> {
    @Override
    public void registerControllers(AzAnimationControllerContainer animationControllerContainer) {
        animationControllerContainer.add(AzAnimationController.builder(this, "base_controller").build());
    }


    @Override
    public @NotNull Identifier getAnimationLocation(DivineDogKuroEntity animatable) {
        return BoundlessAPI.identifier("animations/entity/divine_dog.animation.json");
    }

    @Override
    public void setCustomAnimations(DivineDogKuroEntity animatable, float partialTicks) {
        super.setCustomAnimations(animatable, partialTicks);

        if (animatable.isInSittingPose()) return;

        var model = this.context().boneCache().getBakedModel();

        //var tail = model.getBone("tail");

        var left_front = model.getBone("leg1");
        var right_front = model.getBone("leg2");
        var left_back = model.getBone("leg3");
        var right_back = model.getBone("leg4");

        float swing = animatable.limbAnimator.getPos(partialTicks);
        float amount = animatable.limbAnimator.getSpeed(partialTicks);

        float walkAngle = swing * 0.6662F;

        float right = MathHelper.cos(walkAngle) * 1.4F * amount;
        float left = MathHelper.cos(walkAngle + MathHelper.PI) * 1.4F * amount;

        left_front.ifPresent(bone -> bone.setRotX(right));
        right_front.ifPresent(bone -> bone.setRotX(left));
        left_back.ifPresent(bone -> bone.setRotX(left));
        right_back.ifPresent(bone -> bone.setRotX(right));
    }
}
