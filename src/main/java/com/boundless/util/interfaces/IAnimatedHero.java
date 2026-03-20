package com.boundless.util.interfaces;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import net.minecraft.util.Identifier;

public interface IAnimatedHero {
    ModifierLayer<IAnimation> boundless_getModAnimation();
    Identifier boundless$getLastTriggeredAnimation();
    void boundless$setLastTriggeredAnimation(Identifier identifier);
    int boundless$getAnimationPriority(Identifier identifier, int defaultValue);
    void boundless$setAnimationPriority(Identifier identifier, int priority);
}
