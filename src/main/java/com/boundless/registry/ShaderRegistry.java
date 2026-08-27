package com.boundless.registry;

import com.boundless.BoundlessAPI;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class ShaderRegistry {
    public static final ManagedShaderEffect CLEAVE_RED = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.id("shaders/post/cleave_red.json"));
    public static final ManagedShaderEffect CLEAVE_WHITE = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.id("shaders/post/cleave_white.json"));
    public static final ManagedShaderEffect GRAYSCALE = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.id("shaders/post/grayscale.json"));
    public static final ManagedShaderEffect SHRINE_OVERLAY = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.id("shaders/post/shrine_overlay.json"));
}
