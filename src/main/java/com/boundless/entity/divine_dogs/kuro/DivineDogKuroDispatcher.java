package com.boundless.entity.divine_dogs.kuro;

import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import mod.azure.azurelib.common.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.common.animation.play_behavior.AzPlayBehaviors;

public class DivineDogKuroDispatcher {
    private final DivineDogKuroEntity entity;

    /* Todo: Come back to this soon
    private static final AzCommand ANIMATION_NAME_BEGIN = AzCommand.create(
            "base_controller",
            "domain_begin",
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );
     */

    public DivineDogKuroDispatcher(DivineDogKuroEntity animatable) {
        this.entity = animatable;
    }

    /*
    public void animationNameBegin() {
        ANIMATION_NAME.sendForEntity(entity);
    }
     */
}
