package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.mechanics.ComboManager;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.AnimationManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BarrageComboAbility extends TechniqueAbility {
    public String target = "LLLRR";

    @Override
    public void activate(LivingEntity user) {
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("barrage_combo"));
        ComboManager.resetProgress(user, StrongestComponents.MELEE_COMBO);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("barrage_combo");
    }

    @Override
    public boolean canActivate(LivingEntity user) {
        return ComboManager.getProgress(user, StrongestComponents.MELEE_COMBO).equals(target);
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.literal("Barrage: ").append(ComboManager.formattedComboText(target, ComboManager.getProgress(user, StrongestComponents.MELEE_COMBO)));
    }
}
