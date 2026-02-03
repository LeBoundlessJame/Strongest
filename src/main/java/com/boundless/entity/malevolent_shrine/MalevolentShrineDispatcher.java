package com.boundless.entity.malevolent_shrine;

import mod.azure.azurelib.rewrite.animation.dispatch.command.AzCommand;
import mod.azure.azurelib.rewrite.animation.play_behavior.AzPlayBehaviors;

public class MalevolentShrineDispatcher {
    private final MalevolentShrineEntity entity;
    private static final AzCommand DOMAIN_BEGIN = AzCommand.create(
            "base_controller",
            "domain_begin",
            AzPlayBehaviors.HOLD_ON_LAST_FRAME
    );

    public MalevolentShrineDispatcher(MalevolentShrineEntity animatable) {
        this.entity = animatable;
    }

    public void domainBegin() {
        DOMAIN_BEGIN.sendForEntity(entity);
    }
}
