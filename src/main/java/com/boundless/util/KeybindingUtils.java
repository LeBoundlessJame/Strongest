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
}
