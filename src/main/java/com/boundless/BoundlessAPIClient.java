package com.boundless;

import com.boundless.client.HeroHudRenderer;
import com.boundless.client.KeyInputHandler;
import com.boundless.networking.PayloadRegistry;
import com.boundless.registry.*;
import net.fabricmc.api.ClientModInitializer;

public class BoundlessAPIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderRegistry.initialize();
        KeybindRegistry.initialize();
        KeyInputHandler.keyInputs();
        PayloadRegistry.registerS2CPackets();
        HeroHudRenderer.register();
        ArmorRenderRegistry.initialize();
    }
}
