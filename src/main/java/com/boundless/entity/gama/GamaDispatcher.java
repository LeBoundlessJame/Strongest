package com.boundless.entity.gama;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;
import net.minecraft.entity.LivingEntity;

public class GamaDispatcher {
    private final LivingEntity entity;

    private static final AzCommand IDLE = azLoopedAnim("idle");
    private static final AzCommand HOP = AzCommand.create("base_controller",
            "hop", AzPlayBehaviors.PLAY_ONCE);

    public GamaDispatcher(LivingEntity animatable) {
        this.entity = animatable;
    }

    public void idle() {
        IDLE.sendForEntity(entity);
    }

    public void hop() {
        HOP.sendForEntity(entity);
    }


    public static AzCommand azLoopedAnim(String animation) {
        return AzCommand.create("base_controller", animation, AzPlayBehaviors.LOOP);
    }
}
