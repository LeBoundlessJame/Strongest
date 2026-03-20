package com.boundless.registry;

import com.boundless.BoundlessAPI;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class ShaderRegistry {
    public static final ManagedShaderEffect CLEAVE_RED = registerPostShader("cleave_red");
    public static final ManagedShaderEffect CLEAVE_WHITE = registerPostShader("cleave_white");
    public static final ManagedShaderEffect GRAYSCALE = registerPostShader("grayscale");
    public static final ManagedShaderEffect SHRINE_OVERLAY = registerPostShader("shrine_overlay");

    public static ManagedShaderEffect registerPostShader(String shaderName) {
        return ShaderEffectManager.getInstance().manage(BoundlessAPI.identifier("shaders/post/" + shaderName + ".json"));
    }
}
