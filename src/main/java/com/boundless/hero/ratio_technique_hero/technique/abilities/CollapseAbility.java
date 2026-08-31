package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Getter @Setter @Accessors(chain = true)
public class CollapseAbility extends TechniqueAbility {
    private Identifier animation = BoundlessAPI.id("hook");
    private float damage = 50f;
    private Vec3d radius = new Vec3d(1, 5, 1);

    @Override
    public void activate(PlayerEntity player) {
        System.out.println(damage);
        System.out.println(radius);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("collapse");
    }
}
