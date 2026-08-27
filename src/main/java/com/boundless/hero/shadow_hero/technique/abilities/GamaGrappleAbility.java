package com.boundless.hero.shadow_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.gama.abilities.GamaGrapple;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class GamaGrappleAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        GamaGrapple.grappleLogic(player);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("gama_grapple");
    }

    @Override
    public long getCooldown() {
        return 2;
    }
}
