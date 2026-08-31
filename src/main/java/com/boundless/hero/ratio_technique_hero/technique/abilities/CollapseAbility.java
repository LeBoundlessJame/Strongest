package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.AOEAbility;
import com.boundless.ability.generic.PunchAbility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Getter @Setter @Accessors(chain = true)
public class CollapseAbility extends AOEAbility {
    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("collapse");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 100;
    }
}
