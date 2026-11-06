package com.boundless.hero.yuji.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.megumi.technique.TenShadowsComponents;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.mechanics.ComboManager;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BarrageComboAbility extends TechniqueAbility {
    public String target = "LLLRR";

    @Override
    public void activate(PlayerEntity player) {
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("barrage_combo"));
        ComboManager.resetProgress(player, StrongestComponents.MELEE_COMBO);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("barrage_combo");
    }

    @Override
    public boolean canActivate(PlayerEntity player) {
        return ComboManager.getProgress(player, StrongestComponents.MELEE_COMBO).equals(target);
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.literal("Barrage: ").append(ComboManager.formattedComboText(target, ComboManager.getProgress(player, StrongestComponents.MELEE_COMBO)));
    }
}
