package com.boundless.entity.divine_dogs.kuro;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;

public class DivineDogKuroDispatcher {
    private final DivineDogKuroEntity entity;

    private static final AzCommand IDLE = AzCommand.create(
            "base_controller",
            "idle",
            AzPlayBehaviors.LOOP
    );

    private static final AzCommand LAY_IDLE = AzCommand.create(
            "base_controller",
            "lay_idle",
            AzPlayBehaviors.LOOP
    );

    public DivineDogKuroDispatcher(DivineDogKuroEntity animatable) {
        this.entity = animatable;
    }

    public void playLayIdle() {
        LAY_IDLE.sendForEntity(entity);
    }

    public void playIdle() {
        LAY_IDLE.sendForEntity(entity);
    }
}
