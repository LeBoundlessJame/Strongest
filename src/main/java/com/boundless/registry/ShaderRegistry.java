package com.boundless.registry;

import com.boundless.BoundlessAPI;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

public class ShaderRegistry {
    public static Identifier BLACK_FLASH = BoundlessAPI.identifier("shaders/post/black_flash.json");
    public static Identifier CLEAVE = BoundlessAPI.identifier("shaders/post/cleave_red.json");

    public static final ManagedShaderEffect CLEAVE_RED = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.identifier("shaders/post/cleave_red.json"));
    public static final ManagedShaderEffect CLEAVE_WHITE = ShaderEffectManager.getInstance()
            .manage(BoundlessAPI.identifier("shaders/post/cleave_white.json"));

    private static boolean enabled = true;


}
