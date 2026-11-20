package com.boundless.hero.megumi.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.megumi.technique.TenShadowsComponents;
import com.boundless.hero.megumi.technique.TenShadowsTechnique;
import com.boundless.mechanics.ComboManager;
import com.boundless.util.HeroUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ShikigamiOrderAbility extends TechniqueAbility {
    private static String GAMA = "LRR";
    private String sequenceCharacter;

    public ShikigamiOrderAbility(String sequenceCharacter) {
        this.sequenceCharacter = sequenceCharacter;
    }

    @Override
    public void activate(LivingEntity user) {
        if (ComboManager.updateCombo(user, TenShadowsComponents.CURRENT_ORDER_SEQUENCE, sequenceCharacter, GAMA)) {
            TenShadowsTechnique.GAMA_PULL.use(user);
            TenShadowsTechnique.TOGGLE_SHIKIGAMI_ORDERS_MENU.use(user);
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("shikigami_order_" + this.sequenceCharacter);
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 2;
    }

    @Override
    public boolean canActivate(LivingEntity user) {
        return super.canActivate(user) && HeroUtils.getHeroStack(user).getOrDefault(TenShadowsComponents.SHIKIGAMI_ORDER_MENU, false);
    }
}
