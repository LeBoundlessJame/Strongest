package com.boundless.hero.switcher_hero;

import com.boundless.util.HeroUtils;
import com.boundless.util.RaycastUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;

public class TargetSelectMenu {
    public static void openTargetSelectMenu(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(SwitcherHero.TARGET_SELECT_TIME, player.getWorld().getTime() + 20);

        String displayString = "" + Formatting.AQUA + Formatting.BOLD + "Primary: " + "Left";
        displayString += "" + Formatting.LIGHT_PURPLE + Formatting.OBFUSCATED + Formatting.BOLD +  " | ";
        displayString += "" + Formatting.RED + Formatting.BOLD + "Secondary: " + "Right";

        player.sendMessage(Text.of(displayString), true);
    }

    public static boolean isMenuOpen(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.TARGET_SELECT_TIME, 0L);
    }

    public static void closeMenu(PlayerEntity player) {
        HeroUtils.getHeroStack(player).set(SwitcherHero.TARGET_SELECT_TIME, 0L);
    }

    public static void selectTarget(PlayerEntity player, String targetType) {
        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        ComponentType<Integer> component = targetType.equals("primary") ? SwitcherHero.PRIMARY_TARGET_ID : SwitcherHero.SECONDARY_TARGET_ID;

        HeroUtils.getHeroStack(player).set(component, target.getId());

        if (targetType.equals("primary")) {
            player.sendMessage(Text.of("" + Formatting.AQUA + Formatting.BOLD + "Primary selected: " + target.getDisplayName().getString()), true);
        } else {
            player.sendMessage(Text.of("" + Formatting.RED + Formatting.BOLD + "Secondary selected: " + target.getDisplayName().getString()), true);
        }

        TargetSelectMenu.closeMenu(player);
    }
}
