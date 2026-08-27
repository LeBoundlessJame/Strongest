package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.mechanics.ComboManager;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ShikigamiOrderAbility extends TechniqueAbility {
    private static String GAMA = "LRR";
    private String sequenceCharacter;

    public ShikigamiOrderAbility(String sequenceCharacter) {
        this.sequenceCharacter = sequenceCharacter;
    }

    @Override
    public void activate(PlayerEntity player) {
        if (ComboManager.updateCombo(player, TenShadowsComponents.CURRENT_ORDER_SEQUENCE, sequenceCharacter, GAMA)) {
            System.out.println("Gama!");
        }
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("shikigami_order");
    }

    @Override
    public String getDisplayString() {
        return "Shikigami Order";
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 2;
    }

    @Override
    public boolean canActivate(PlayerEntity player) {
        return super.canActivate(player) && HeroUtils.getHeroStack(player).getOrDefault(TenShadowsComponents.SHIKIGAMI_ORDER_MENU, false);
    }
}
