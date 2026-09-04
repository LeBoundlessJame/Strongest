package com.boundless.hero.nanami.client;

import com.boundless.BoundlessAPI;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.nanami.technique.RatioComponents;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class NanamiRenderer extends HeroArmorRenderer {
    private static Identifier MODEL = BoundlessAPI.modelID("hero");
    private static Identifier NORMAL = BoundlessAPI.textureID("nanami");
    private static Identifier OVERTIME = BoundlessAPI.textureID("nanami_wrapped");

    public NanamiRenderer(Identifier model, Identifier texture) {
        super(NanamiRenderer::getModel, NanamiRenderer::getTexture);
    }

    private static Identifier getTexture(Entity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity player && getOvertimeTicks(player) >= 5) return OVERTIME;
        return NORMAL;
    }

    private static Identifier getModel(Entity entity, ItemStack stack) {
        return MODEL;
    }

    private static int getOvertimeTicks(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(RatioComponents.OVERTIME_ELAPSED, 0);
    }
}
