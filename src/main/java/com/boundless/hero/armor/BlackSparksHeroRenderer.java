package com.boundless.hero.armor;

import com.boundless.BoundlessAPI;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import mod.azure.azurelib.common.render.armor.AzArmorRendererConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BlackSparksHeroRenderer extends AzArmorRenderer {
    public static Identifier TEXTURE = BoundlessAPI.textureID("black_sparks_hero");
    public static Identifier CHARGING_ENERGY = BoundlessAPI.textureID("black_sparks_hero_charging");

    public BlackSparksHeroRenderer(Identifier model, Identifier texture) {
        super(AzArmorRendererConfig.builder(((entity, itemStack) -> model), (entity, stack) -> {
            if (!(entity instanceof PlayerEntity player)) return TEXTURE;
            if (channelingCursedEnergy(player, stack)) return CHARGING_ENERGY;
            return TEXTURE;
        }).build());
    }

    public static boolean channelingCursedEnergy(PlayerEntity player, ItemStack stack) {
        long minigameEnd = stack.getOrDefault(BlackSparksHero.MINIGAME_END_TIMESTAMP, player.getWorld().getTime());
        long ceTimestamp = stack.getOrDefault(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());

        return player.getWorld().getTime() <= minigameEnd || player.getWorld().getTime() <= ceTimestamp;
    }
}