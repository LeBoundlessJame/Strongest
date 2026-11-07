package com.boundless.hero.megumi.technique;

import com.boundless.combat.CombatAbilities;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class TenShadowsAbilityResolver {

    public static Identifier getRightClickAbility(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.SHIKIGAMI_ORDER_RIGHT.getId();
        return player.getVehicle() instanceof GamaEntity ? TenShadowsTechnique.GAMA_GRAPPLE.getId() : CombatAbilities.ROUNDHOUSE_KICK.getId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.SHIKIGAMI_ORDER_LEFT.getId();
        return CombatAbilities.PUNCH.getId();
    }

    public static Identifier getAbilityOne(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.KURO.getId();
        return null;
    }

    public static Identifier getAbilityTwo(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.SHIRO.getId();
        return null;
    }

    public static Identifier getAbilityThree(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.GAMA.getId();
        return null;
    }

    public static Identifier getGamaPull(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.GAMA_PULL.getId();
        return null;
    }

    private static boolean isOrderMenu(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(TenShadowsComponents.SHIKIGAMI_ORDER_MENU, false);
    }
}
