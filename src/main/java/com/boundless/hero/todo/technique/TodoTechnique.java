package com.boundless.hero.todo.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.KickAbility;
import com.boundless.combat.CombatAbilities;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.megumi.technique.TenShadowsTechnique;
import com.boundless.hero.megumi.technique.abilities.ShikigamiOrdersMenuAbility;
import com.boundless.hero.todo.technique.abilities.*;
import com.boundless.registry.StrongestComponents;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class TodoTechnique {
    public static final TechniqueAbility BOOGIE_WOOGIE = TechniqueAbilityRegistry.register(new BoogieWoogieAbility());
    public static final TechniqueAbility SUPLEX = TechniqueAbilityRegistry.register(new SuplexAbility());
    public static final TechniqueAbility BOOGIE_WOOGIE_SWAP_TWO = TechniqueAbilityRegistry.register(new SwapTwoAbility());
    public static final TechniqueAbility SELECT_PRIMARY_TARGET = TechniqueAbilityRegistry.register(new SelectTargetAbility(SelectTargetAbility.Target.PRIMARY));
    public static final TechniqueAbility SELECT_SECONDARY_TARGET = TechniqueAbilityRegistry.register(new SelectTargetAbility(SelectTargetAbility.Target.SECONDARY));
    public static final TechniqueAbility SELECT_TARGET_MENU = TechniqueAbilityRegistry.register(new SelectTargetMenu());

    public static Identifier getSwapEnabled(PlayerEntity player) {
        if (menuOpen(player)) return BOOGIE_WOOGIE_SWAP_TWO.getId();
        return SUPLEX.getId();
    }

    public static Identifier getLeftClickAbility(PlayerEntity player) {
        if (menuOpen(player)) return SELECT_PRIMARY_TARGET.getId();
        return CombatAbilities.PUNCH.getId();
    }

    public static Identifier getRightClickAbility(PlayerEntity player) {
        if (menuOpen(player)) return SELECT_SECONDARY_TARGET.getId();
        return CombatAbilities.ROUNDHOUSE_KICK.getId();
    }

    private static boolean menuOpen(PlayerEntity player) {
        return ComponentUtils.getOr(player, StrongestComponents.MENU_TOGGLE, false);
    }
}
