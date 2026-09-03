package com.boundless.util;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.mixin.KeybindAccessor;
import com.boundless.registry.DataComponentRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class KeybindingUtils {
    public static KeyBinding getKeyBindingFromTranslation(String translation) {
        Map<String, KeyBinding> keysByID = KeybindAccessor.getKeysByID();
        return keysByID.get(translation);
    }

    public static KeybindHoldData getHoldData(PlayerEntity player, String key) {
        Map<String, KeybindHoldData> keyHeldMap = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.HELD_KEYBIND, new HashMap<>());
        return keyHeldMap.getOrDefault(key, new KeybindHoldData(false, 0, 0));
    }

    public static void endKeybindHold(PlayerEntity player, String key) {
        ComponentUtils.updateMap(HeroUtils.getHeroStack(player), DataComponentRegistry.HELD_KEYBIND, key, new KeybindHoldData(false, 0L, 0L));
    }
}
