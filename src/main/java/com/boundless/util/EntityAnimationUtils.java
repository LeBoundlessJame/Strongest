package com.boundless.util;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.dispatch.command.AzControllerCommandBuilder;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehavior;

public class EntityAnimationUtils {

    private static AzControllerCommandBuilder controllerBuilder() {
        return new AzControllerCommandBuilder();
    }

    public static AzCommand create(String controllerName, String animationName, AzPlayBehavior playBehavior, float transitionLength) {
        return controllerBuilder().playSequence(controllerName, sequenceBuilder -> sequenceBuilder.queue(animationName, props -> props.withPlayBehavior(playBehavior))).setFreezeTickOffset(controllerName, 0).setStartTickOffset(controllerName, 0).setSpeed(controllerName, 1.0f).setTransitionSpeed(controllerName, transitionLength).build();
    }
}
