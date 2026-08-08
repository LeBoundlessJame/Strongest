package com.boundless.entity.divine_dogs.kuro;

import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;

public class DivineDogKuroDispatcher {
    private final DivineDogKuroEntity entity;

    private static final AzCommand IDLE = azLoopedAnim("dog_idle");
    private static final AzCommand LAY_IDLE = azLoopedAnim("dog_laying_idle");
    private static final AzCommand WALK = azLoopedAnim("dog_walking");
    private static final AzCommand RUN = azLoopedAnim("dog_running");
    private static final AzCommand SLASH = AzCommand.create("base_controller", "dog_slash", AzPlayBehaviors.PLAY_ONCE);

    public DivineDogKuroDispatcher(DivineDogKuroEntity animatable) {
        this.entity = animatable;
    }

    public void layIdle() {
        LAY_IDLE.sendForEntity(entity);
    }

    public void idle() {
        IDLE.sendForEntity(entity);
    }

    public void slash() {
        SLASH.sendForEntity(entity);
    }

    public void walk() {
        WALK.sendForEntity(entity);
    }

    public void run() {
        RUN.sendForEntity(entity);
    }

    public static AzCommand azLoopedAnim(String animation) {
        return AzCommand.create("base_controller", animation, AzPlayBehaviors.LOOP);
    }
}
