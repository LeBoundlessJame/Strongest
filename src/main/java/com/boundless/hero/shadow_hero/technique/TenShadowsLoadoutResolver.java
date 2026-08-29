package com.boundless.hero.shadow_hero.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class TenShadowsLoadoutResolver {

    public static Identifier getRightClickAbility(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.SHIKIGAMI_ORDER_RIGHT.getAbilityId();
        return player.getVehicle() instanceof GamaEntity ? TenShadowsTechnique.GAMA_GRAPPLE.getAbilityId() : TenShadowsTechnique.ROUNDHOUSE_KICK.getAbilityId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.SHIKIGAMI_ORDER_LEFT.getAbilityId();
        return TenShadowsTechnique.PUNCH.getAbilityId();
    }

    public static Identifier getAbilityOne(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.KURO.getAbilityId();
        return TechniqueAbilityRegistry.EMPTY.getAbilityId();
    }

    public static Identifier getAbilityTwo(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.SHIRO.getAbilityId();
        return TechniqueAbilityRegistry.EMPTY.getAbilityId();
    }

    public static Identifier getAbilityThree(PlayerEntity player) {
        if (!isOrderMenu(player)) return TenShadowsTechnique.GAMA.getAbilityId();
        return TechniqueAbilityRegistry.EMPTY.getAbilityId();
    }

    public static Identifier getGamaPull(PlayerEntity player) {
        if (isOrderMenu(player)) return TenShadowsTechnique.GAMA_PULL.getAbilityId();
        return TechniqueAbilityRegistry.EMPTY.getAbilityId();
    }

    private static boolean isOrderMenu(PlayerEntity player) {
        return HeroUtils.getHeroStack(player).getOrDefault(TenShadowsComponents.SHIKIGAMI_ORDER_MENU, false);
    }
}
