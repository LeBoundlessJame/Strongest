package com.boundless.hero.yuji.technique;

import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.CombatAbilities;
import com.boundless.hero.yuji.technique.abilities.BarrageComboAbility;
import com.boundless.hero.yuji.technique.abilities.DivergentFistAbility;
import com.boundless.hero.yuji.technique.abilities.DropkickAbility;
import com.boundless.hero.yuji.technique.abilities.ManjiKickAbility;
import com.boundless.registry.TechniqueAbilityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class YujiTechnique {
    public static final TechniqueAbility DIVERGENT_FIST = TechniqueAbilityRegistry.register(new DivergentFistAbility());
    public static final TechniqueAbility MANJI_KICK = TechniqueAbilityRegistry.register(new ManjiKickAbility());
    public static final TechniqueAbility BARRAGE_COMBO = TechniqueAbilityRegistry.register(new BarrageComboAbility());

    public static Identifier kickAbilityResolver(PlayerEntity player) {
        return player.isOnGround() ? MANJI_KICK.getAbilityId() : CombatAbilities.DROPKICK.getAbilityId();
    }
}
