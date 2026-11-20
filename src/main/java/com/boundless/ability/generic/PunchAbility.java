package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.HitEffects;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.tick.TickScheduler;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

@Getter @Setter @Accessors(chain = true)
public class PunchAbility extends TechniqueAbility {
    private Identifier abilityId;

    private int impactTick = 2;
    private int attackDuration = 8;

    private Identifier animation = BoundlessAPI.id("hook");
    private float animationSpeed = 1.0f;

    private SoundEvent whiffSound = SoundRegistry.MISS_HIT;
    private HitEffects impactEffects = new HitEffects(List.of(BoundlessAPI.id("melee_impact")), List.of(SoundRegistry.IMPACT_HEAVY_1));

    private Consumer<LivingEntity> preAttackEvent = (user) -> {
    };
    private BiConsumer<LivingEntity, LivingEntity> onHitEvent = (user, target) -> {
    };
    private Consumer<LivingEntity> postAttackEvent = (user) -> {
    };

    private Vec3d knockback = new Vec3d(0.6, 0.3, 0.6);

    private Consumer<LivingEntity> impactEventOverride;

    private Function<LivingEntity, Boolean> mirrorAnimationProvider = (user) -> {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, user, 1);
        int attackCount = HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        return attackCount % 2 == 0;
    };

    @Override
    public void activate(LivingEntity user) {
        if (!(user.getWorld().getTime() >= HeroUtils.getHeroStack(user).getOrDefault(ATTACK_END, 0L))) return;

        preAttackEvent.accept(user);
        AnimationManager.playSyncedAnimation(user, this.animation, this.animationSpeed, mirrorAnimationProvider.apply(user), true, 3000);
        SoundUtils.playSound(user, this.whiffSound);

        TickScheduler.schedule(user.getWorld(), this.impactTick, () -> this.impact(user));
        AttackUtils.startAttackTimer(user, this.attackDuration);
    }

    private void impact(LivingEntity user) {
        if (impactEventOverride != null) {
            impactEventOverride.accept(user);
            postAttackEvent.accept(user);
            return;
        }

        List<LivingEntity> targets = new MeleeHitbox(5.0, 180.0).getTargetsInArc(user);
        CombatUtils.applyHits(user, targets, this.getDamage(user), knockback, onHitEvent, impactEffects);

        postAttackEvent.accept(user);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("punch");
    }

    public float getDamage(LivingEntity user) {
        return ComponentUtils.getOr(user, StrongestComponents.MELEE_STRENGTH, 0f);
    }
}
