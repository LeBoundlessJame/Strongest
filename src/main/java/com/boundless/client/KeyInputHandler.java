package com.boundless.client;

import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.mechanics.AbilityManager;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null || !HeroUtils.isHero(client.player)) return;

            TechniqueLoadout loadout = HeroUtils.getTechniqueLoadout(client.player);

            if (loadout == null) return;

            for (AbilityKey key : AbilityKey.values()) {
                inputLogic(client, key, loadout);
            }
        });
    }

    private static void inputLogic(MinecraftClient client, AbilityKey abilityKey, TechniqueLoadout loadout) {
        KeyBinding key = KeybindingUtils.getKeyBindingFromTranslation(abilityKey.getTranslationKey());
        if (key == null || !isKeybindingPressed(key)) return;

        Identifier abilityID = loadout.getAbilityId(abilityKey, client.player);

        if (!AbilityManager.checkAndUseTechniqueAbility(client.player, abilityID)) {
            return;
        }

        ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
    }

    private static boolean isKeybindingPressed(KeyBinding keyBinding) {
        InputUtil.Key key = InputUtil.fromTranslationKey(keyBinding.getBoundKeyTranslationKey());
        if (key == null) return false;

        long handle = MinecraftClient.getInstance().getWindow().getHandle();

        if (key.getCategory() == InputUtil.Type.KEYSYM) {
            return InputUtil.isKeyPressed(handle, key.getCode());
        } else if (key.getCategory() == InputUtil.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(handle, key.getCode()) == GLFW.GLFW_PRESS;
        }

        return false;
    }
}
