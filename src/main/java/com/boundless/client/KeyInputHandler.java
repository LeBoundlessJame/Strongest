package com.boundless.client;

import com.boundless.loadouts.AbilityKey;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

public class KeyInputHandler {
    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null || !HeroUtils.isHero(client.player)) return;

            TechniqueLoadout loadout = HeroUtils.getTechniqueLoadout(client.player);

            if (loadout == null) return;

            for (AbilityKey key : AbilityKey.values()) {
                inputLogic(client, key, loadout);
            }
        });
    }

    private static void inputLogic(MinecraftClient client, AbilityKey abilityKey, TechniqueLoadout loadout) {
        KeyBinding key = KeybindingUtils.getKeyBindingFromTranslation(abilityKey.getTranslationKey());
        if (!key.isPressed()) return;

        Identifier abilityID = loadout.getAbilityId(abilityKey, client.player);

        if (!AbilityUtils.checkAndUseTechniqueAbility(client.player, abilityID)) {
            return;
        }

        ClientPlayNetworking.send(new AbilityUsePayload(abilityID));
    }
}
