package com.boundless.registry;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindRegistry {
    public static KeyBinding ABILITY_ONE = registerKeybinding("ability_one", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R);
    public static KeyBinding ABILITY_TWO = registerKeybinding("ability_two", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F);
    public static KeyBinding ABILITY_THREE = registerKeybinding("ability_three", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C);
    public static KeyBinding ABILITY_FOUR = registerKeybinding("ability_four", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X);
    public static KeyBinding ABILITY_FIVE = registerKeybinding("ability_five", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z);
    public static KeyBinding COMBAT_MODE_TOGGLE = registerKeybinding("combat_mode_toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_TAB);

    public static void initialize() {}

    public static KeyBinding registerKeybinding(String keybindName, InputUtil.Type keybindType, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.boundless." + keybindName,
                keybindType,
                key,
                "category.boundless.controls"
        ));
    }
}
