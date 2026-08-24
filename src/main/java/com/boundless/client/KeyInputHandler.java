package com.boundless.client;

import com.boundless.ability.Ability;
import com.boundless.hero.api.HeroData;
import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadoutComponent;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.networking.payloads.UpdateHoldStatePayload;
import com.boundless.registry.DataComponentRegistry;
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
            TechniqueLoadoutComponent loadout = stack.get(DataComponentRegistry.TECHNIQUE_LOADOUT);

            if (loadout == null) return;

            for (AbilityKey key : loadout.abilities().keySet()) {
                inputLogic(client, key, loadout);
            }
        });
    }

    private static void inputLogic(MinecraftClient client, AbilityKey abilityKey, TechniqueLoadoutComponent loadout) {
        KeyBinding key = KeybindingUtils.getKeyBindingFromTranslation(abilityKey.getTranslationKey());
        if (!key.isPressed()) return;

        Identifier abilityID = loadout.abilities().get(abilityKey);
        if (!AbilityUtils.checkAndUseAbility(client.player, abilityID)) return;

        ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
    }
}
