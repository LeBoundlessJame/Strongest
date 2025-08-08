package com.boundless.client;

import com.boundless.hero.api.HeroData;
import com.boundless.networking.payloads.AbilityUsePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Consumer;

public class KeyInputHandler {
    public static void keyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player == null || !HeroUtils.isHero(client.player)) return;
            ItemStack stack = HeroUtils.getHeroStack(client.player);
            Map<String, Identifier> abilities = stack.get(DataComponentRegistry.ABILITY_LOADOUT);
            if (abilities == null) return;

            for (String translatableKey: abilities.keySet()) {
                KeybindingUtils.inputLogic(client, translatableKey);
            }

            HeroData heroData = HeroUtils.getHeroData(client.player);
            if (heroData == null) return;
            for (Consumer<MinecraftClient> clientConsumer: heroData.getClientTickEvents()) {
                clientConsumer.accept(client);
            }
        });
    }
}
