package com.boundless.entity.divine_dogs.kuro;

import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;

public class DivineDogKuroDispatcher {
    private final DivineDogKuroEntity entity;

    private static final AzCommand LAY_BEGIN = AzCommand.create(
            "base_controller",
            "lay_idle",
            AzPlayBehaviors.LOOP
    );

    public DivineDogKuroDispatcher(DivineDogKuroEntity animatable) {
        this.entity = animatable;
    }

    public void layBegin() {
        LAY_BEGIN.sendForEntity(entity);
    }
}
