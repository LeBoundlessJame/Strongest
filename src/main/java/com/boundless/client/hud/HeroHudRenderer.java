package com.boundless.client.hud;

import com.boundless.gui.HeroHUD;
import com.boundless.hero.api.HeroData;
import com.boundless.util.HeroUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public class HeroHudRenderer {
    public static void register() {
        HudRenderCallback.EVENT.register(((drawContext, renderTickCounter) -> {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            if (minecraftClient == null || minecraftClient.player == null || !HeroUtils.isHero(minecraftClient.player)) return;
            HeroData heroData = HeroUtils.getHeroData(minecraftClient.player);
            if (heroData == null) return;
            if (heroData.getHudRenderer() != null) {
                heroData.getHudRenderer().accept(drawContext, renderTickCounter);
            } else {
                HeroHUD.render(drawContext, renderTickCounter);
            }
        }));
    }
}
