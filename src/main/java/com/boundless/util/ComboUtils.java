package com.boundless.util;

import com.boundless.combat.Combo;
import com.boundless.combat.MeleeAbility;
import com.boundless.hero.api.HeroData;
import net.minecraft.entity.player.PlayerEntity;

public class ComboUtils {
    /** returns if a combo has been triggered or not **/
    public static boolean evaluateCombos(PlayerEntity player, MeleeAbility ability) {
        HeroData heroData = HeroUtils.getHeroData(player);
        if (heroData == null) return false;
        boolean evaluated = false;
        for (Combo combo: heroData.getCombos()) {
            if (combo.matchesTargetCombo(player, ability.getComboLetter())) {
                evaluated = true;
            }
            combo.updateAndEvaluateCombo(player, ability.getComboLetter());
            if (evaluated) return evaluated;
        }
        return false;
    }
}
