package com.boundless.entity.finger_bearer;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;

public class FingerBearerDispatcher {
    private final FingerBearerEntity entity;
    private static final AzCommand WALK = azLoopedAnim("walk");
    private static final AzCommand CHARGE_UP = azLoopedAnim("charge_up");

    public FingerBearerDispatcher(FingerBearerEntity animatable) {
        this.entity = animatable;
    }

    public void walk() {
        WALK.sendForEntity(entity);
    }

    public void chargeUp() {
        CHARGE_UP.sendForEntity(entity);
    }

    public static AzCommand azLoopedAnim(String animation) {
        return AzCommand.create("base_controller", animation, AzPlayBehaviors.LOOP);
    }
}