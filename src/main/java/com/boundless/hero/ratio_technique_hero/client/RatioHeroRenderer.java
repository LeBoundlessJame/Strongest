package com.boundless.hero.ratio_technique_hero.client;

import com.boundless.BoundlessAPI;
import com.boundless.hero.armor.HeroArmorRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class RatioHeroRenderer extends HeroArmorRenderer {
    private static Identifier MODEL = BoundlessAPI.modelID("ratio_technique_hero");
    private static Identifier NORMAL = BoundlessAPI.textureID("nanami");
    private static Identifier OVERTIME = BoundlessAPI.textureID("nanami_wrapped");

    public RatioHeroRenderer(Identifier model, Identifier texture) {
        super(RatioHeroRenderer::getModel, RatioHeroRenderer::getTexture);
    }

    private static Identifier getTexture(Entity entity, ItemStack stack) {
        return entity.isSneaking() ? OVERTIME : NORMAL;
    }

    private static Identifier getModel(Entity entity, ItemStack stack) {
        return MODEL;
    }
}
