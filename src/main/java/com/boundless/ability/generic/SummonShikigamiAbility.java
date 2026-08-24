package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.util.Shikigami;
import com.boundless.util.ShikigamiUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class SummonShikigamiAbility<T extends TameableEntity & Shikigami> extends TechniqueAbility {
    private EntityType<T> shikigamiType;

    public SummonShikigamiAbility(EntityType<T> shikigamiType) {
        this.shikigamiType = shikigamiType;
    }

    @Override
    public void activate(PlayerEntity playerEntity) {
        ShikigamiUtils.toggleShikigami(playerEntity, this.shikigamiType);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.identifier("summon_" + shikigamiType.toString());
    }
}
