package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.action.Action;
import com.boundless.combat.HitEffects;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.BiConsumer;

@Getter @Setter @Accessors(chain = true)
public class AOEAbility extends TechniqueAbility implements AOE {
    private Identifier animation;
    private float damage;
    private int impactTick = 10;
    private int duration = 10;
    private Vec3d radius = new Vec3d(3, 3, 3);
    private Vec3d knockback = new Vec3d(1, 0.5, 1);
    private BiConsumer<PlayerEntity, LivingEntity> onHit = (player, target) -> {};
    private HitEffects hitEffects = new HitEffects(List.of(BoundlessAPI.id("melee_impact")), List.of(SoundRegistry.IMPACT_HEAVY_1));

    @Override
    public void activate(PlayerEntity player) {
        Action action = Action.builder().scheduledTask(this.getImpactTick(), this::activateAOE).build();
        ActionUtils.performAction(player, action);
    }

    @Override
    public Identifier getAbilityId() {
        return this.getAbilityId();
    }
}
