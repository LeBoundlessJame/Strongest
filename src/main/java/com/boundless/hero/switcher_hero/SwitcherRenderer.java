package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.util.HeroUtils;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SwitcherRenderer extends AzArmorRenderer {
    public static Identifier TEXTURE = BoundlessAPI.textureID("switcher");
    public static Identifier DUO_TEXTURE = BoundlessAPI.textureID("duo");

    public static Identifier MODEL = BoundlessAPI.modelID("switcher");
    public static Identifier DUO_MODEL = BoundlessAPI.modelID("duo");

    public SwitcherRenderer(Identifier model, Identifier texture) {
        super(AzArmorRendererConfig.builder(((entity, itemStack) -> {
            if (!(entity instanceof PlayerEntity player)) return MODEL;
            if (isRevivedRecently(player, HeroUtils.getHeroStack(player))) return DUO_MODEL;
            return MODEL;

        }), (entity, stack) -> {
            if (!(entity instanceof PlayerEntity player)) return TEXTURE;
            if (isRevivedRecently(player, HeroUtils.getHeroStack(player))) return DUO_TEXTURE;
            return TEXTURE;
        }).build());
    }

    public static boolean isRevivedRecently(PlayerEntity player, ItemStack stack) {
        long lastRevive = stack.getOrDefault(SwitcherHero.LAST_REVIVE_TIMESTAMP, 0L);

        return player.getWorld().getTime() > lastRevive + 15 && player.getWorld().getTime() < lastRevive + SwitcherHero.CONFIG.reviveBuffDuration.get();
    }
}