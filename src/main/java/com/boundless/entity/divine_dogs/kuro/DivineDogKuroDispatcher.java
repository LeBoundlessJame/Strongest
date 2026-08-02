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

    private static final AzCommand WALK = AzCommand.create(
            "base_controller",
            "walk",
            AzPlayBehaviors.LOOP
    );

    private static final AzCommand RUN = AzCommand.create(
            "base_controller",
            "run",
            AzPlayBehaviors.LOOP
    );

    public DivineDogKuroDispatcher(DivineDogKuroEntity animatable) {
        this.entity = animatable;
    }

    public void layIdle() {
        LAY_IDLE.sendForEntity(entity);
    }

    public void idle() {
        IDLE.sendForEntity(entity);
    }

    public void walk() {
        WALK.sendForEntity(entity);
    }

    public void run() {
        RUN.sendForEntity(entity);
    }
}
