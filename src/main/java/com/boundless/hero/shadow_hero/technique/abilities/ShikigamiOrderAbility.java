package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.shadow_hero.technique.TenShadowsComponents;
import com.boundless.hero.shadow_hero.technique.TenShadowsTechnique;
import com.boundless.mechanics.ComboManager;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
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
            TenShadowsTechnique.GAMA_PULL.use(player);
            TenShadowsTechnique.TOGGLE_SHIKIGAMI_ORDERS_MENU.use(player);
        }
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("shikigami_order_" + this.sequenceCharacter);
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
