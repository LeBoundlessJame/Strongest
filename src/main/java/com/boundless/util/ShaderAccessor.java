package com.boundless.util;

import net.minecraft.util.Identifier;

public interface ShaderAccessor {
    void boundless$disablePostProcessor();
    void boundless$loadShader(Identifier identifier);
}
