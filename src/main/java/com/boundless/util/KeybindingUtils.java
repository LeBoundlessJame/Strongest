package com.boundless.util;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.mixin.KeybindAccessor;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.registry.DataComponentRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class KeybindingUtils {
    public static KeyBinding getKeyBindingFromTranslation(String translation) {
        Map<String, KeyBinding> keysByID = KeybindAccessor.getKeysByID();
        return keysByID.get(translation);
    }

    public static void inputLogic(MinecraftClient client, String translatableKey) {
        if (KeybindingUtils.getKeyBindingFromTranslation(translatableKey).isPressed()) {
            Identifier abilityID = AbilityUtils.abilityIDFromKeybind(client.player, translatableKey);
            if (AbilityUtils.checkAndUseAbility(client.player, abilityID)) {
                ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
            };
        }
    }

    private static final Map<String, Boolean> heldKeysMap = new HashMap<>();

    public static void keybindHoldLogic(MinecraftClient client, KeyBinding key, String translationKey) {
        if (client.player == null) return;

        if (key.isPressed() && !heldKeysMap.getOrDefault(translationKey, false)) {
            heldKeysMap.put(translationKey, true);
            ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, true));
        } else if (!key.isPressed() && heldKeysMap.getOrDefault(translationKey, false)) {
            heldKeysMap.put(translationKey, false);
            ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, false));
        }
    }
}
