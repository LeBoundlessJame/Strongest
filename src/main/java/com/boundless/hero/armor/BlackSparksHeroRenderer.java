package com.boundless.hero.armor;

import com.boundless.BoundlessAPI;
import com.boundless.hero.black_sparks_hero.BrawlerHero;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

// Todo: come back to this
public class BlackSparksHeroRenderer extends AzArmorRenderer {
    public static Identifier TEXTURE = BoundlessAPI.textureID("black_sparks_hero");
    public static Identifier CHARGING_1 = BoundlessAPI.textureID("black_sparks_hero_charging_1");
    public static Identifier CHARGING_2 = BoundlessAPI.textureID("black_sparks_hero_charging_2");

    public static boolean inBlackFlashMinigame(PlayerEntity player, ItemStack stack) {
        long minigameEnd = stack.getOrDefault(BrawlerHero.MINIGAME_END_TIMESTAMP, 0L);

        return player.getWorld().getTime() <= minigameEnd;
    }
}