package com.boundless.combat.attack_modifiers;

import com.boundless.BoundlessAPI;
import com.boundless.combat.*;
import com.boundless.hero.yuji.technique.YujiComponents;
import com.boundless.hero.yuji.technique.components.DivergentTarget;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CameraUtils;
import com.boundless.util.CombatUtils;
import com.boundless.util.ComponentUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class DivergentModifier implements AttackModifier {
    private static final int DIVERGENT_DELAY_TICKS = 11;
    private static final float FIRST_HIT_MULTIPLIER = 0.5f;
    private static final float SECOND_HIT_MULTIPLIER = 0.6f;

    @Override
    public boolean shouldTrigger(PlayerEntity player) {
        return ComponentUtils.getOr(player, YujiComponents.DIVERGENCE_ACTIVE, false);
    }

    @Override
    public void apply(Hit hit) {
        PlayerEntity player = hit.getAttacker();

        float originalDamage = hit.getDamage();
        hit.multiplyDamage(FIRST_HIT_MULTIPLIER);

        List<DivergentTarget> currentTargets = ComponentUtils.getOr(player, YujiComponents.DIVERGENT_TARGETS, List.of());
        List<DivergentTarget> updated = new ArrayList<>(currentTargets);
        updated.add(new DivergentTarget(hit.getTarget().getUuid(), originalDamage));

        ComponentUtils.set(player, YujiComponents.DIVERGENT_TARGETS, updated);
    }

    @Override
    public void postTrigger(PlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return;

        List<DivergentTarget> targets = ComponentUtils.getOr(player, YujiComponents.DIVERGENT_TARGETS, List.of());

        if (!targets.isEmpty()) {
            TickScheduler.schedule(serverWorld, DIVERGENT_DELAY_TICKS, () -> {
                if (!player.isAlive()) return;

                CameraUtils.playCameraShake(player);
                player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 5, 4, true, false, false));

                AttackContext context = AttackResolver.resolveAttack(player);

                for (DivergentTarget target: targets) {
                    Entity entity = serverWorld.getEntity(target.uuid());
                    if (!(entity instanceof LivingEntity livingEntity && livingEntity.isAlive())) continue;

                    HitEffects divergentEffects = new HitEffects();
                    divergentEffects.addVisual(BoundlessAPI.id("divergent_fist_impact"));
                    divergentEffects.addSound(SoundRegistry.DIVERGENT_IMPACT);

                    Hit delayedHit = new Hit(player, livingEntity, target.damage() * SECOND_HIT_MULTIPLIER, new Vec3d(0.6, 0.3, 0.6), divergentEffects);

                    CombatUtils.resolveAndApplyHit(delayedHit, context);
                }
            });
        }

        ComponentUtils.set(player, YujiComponents.DIVERGENT_TARGETS, List.of());
        ComponentUtils.set(player, YujiComponents.DIVERGENCE_ACTIVE, false);
    }
}
