package com.boundless.hero.armor;

import com.boundless.BoundlessAPI;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class BlackSparksHeroRenderer extends AzArmorRenderer {
    public static Identifier TEXTURE = BoundlessAPI.textureID("black_sparks_hero");
    public static Identifier CHARGING_ENERGY = BoundlessAPI.textureID("black_sparks_hero_charging");

    public BlackSparksHeroRenderer(Identifier model, Identifier texture) {
        super(AzArmorRendererConfig.builder(((entity, itemStack) -> model), (entity, stack) -> {
            if (entity instanceof PlayerEntity player && player.isSneaking()) {
                return CHARGING_ENERGY;
            }
            return TEXTURE;
        }).build());
    }
}