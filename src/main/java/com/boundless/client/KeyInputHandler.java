package com.boundless.client;

import com.boundless.hero.api.HeroData;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.KeybindRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler {
    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null || !HeroUtils.isHero(client.player)) return;
            ItemStack stack = HeroUtils.getHeroStack(client.player);
            Map<String, Identifier> abilities = stack.get(DataComponentRegistry.ABILITY_LOADOUT);
            if (abilities == null) return;

            for (String translatableKey: abilities.keySet()) {
                inputLogic(client, translatableKey);
            }

            HeroData heroData = HeroUtils.getHeroData(client.player);
            if (heroData == null) return;
            /* Todo: reintegrate some other stage
            for (Consumer<MinecraftClient> clientConsumer: heroData.getClientTickEvents()) {
                clientConsumer.accept(client);
            } */
            KeyInputHandler.keybindHoldLogic(client, client.options.forwardKey.getTranslationKey());
            KeyInputHandler.keybindHoldLogic(client, client.options.backKey.getTranslationKey());

            if (heroData.getHeldKeybinds() == null) return;

            for (String keybind: heroData.getHeldKeybinds()) {
                KeyInputHandler.keybindHoldLogic(client, keybind);
            }
        });
    }

    private static final Map<String, Boolean> heldKeysMap = new HashMap<>();

    public static void keybindHoldLogic(MinecraftClient client, String translationKey) {
        if (client.player == null) return;

        KeyBinding key = KeybindingUtils.getKeyBindingFromTranslation(translationKey);

        if (key.isPressed() && !heldKeysMap.getOrDefault(translationKey, false)) {
            heldKeysMap.put(translationKey, true);
            ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, true));
        } else if (!key.isPressed() && heldKeysMap.getOrDefault(translationKey, false)) {
            heldKeysMap.put(translationKey, false);
            ClientPlayNetworking.send(new UpdateHoldStatePayload(translationKey, false));
        }
    }

    public static void inputLogic(MinecraftClient client, String translatableKey) {
        if (KeybindingUtils.getKeyBindingFromTranslation(translatableKey).isPressed()) {
            Identifier abilityID = AbilityUtils.abilityIDFromKeybind(client.player, translatableKey);
            if (AbilityUtils.checkAndUseAbility(client.player, abilityID)) {
                ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
            };
        }
    }
}
