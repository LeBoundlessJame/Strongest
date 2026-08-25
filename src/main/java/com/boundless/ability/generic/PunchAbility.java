package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import lombok.Builder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

@Builder
public class PunchAbility extends TechniqueAbility {
    private Identifier abilityId;

    @Builder.Default
    private int impactTick = 2;
    @Builder.Default
    private int attackDuration = 10;
    private float damage;

    @Builder.Default
    private Identifier animation = BoundlessAPI.identifier("hook");
    @Builder.Default
    private float animationSpeed = 1.0f;

    @Builder.Default
    private SoundEvent whiffSound = SoundEvents.INTENTIONALLY_EMPTY;
    @Builder.Default
    private SoundEvent impactSound = SoundEvents.INTENTIONALLY_EMPTY;

    @Builder.Default
    private Consumer<PlayerEntity> preAttackEvent = (player) -> {
    };
    @Builder.Default
    private BiConsumer<PlayerEntity, LivingEntity> onHitEvent = (player, target) -> {
    };
    @Builder.Default
    private Consumer<PlayerEntity> postAttackEvent = (player) -> {
    };

    @Builder.Default
    private Vec3d knockback = new Vec3d(0.6, 0.3, 0.6);

    @Override
    public void activate(PlayerEntity player) {
        if (!(player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L))) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        preAttackEvent.accept(player);
        PlayerAnimationUtils.playSyncedAnimation(player, this.animation, this.animationSpeed, attackCount % 2 == 0, true, 3000);
        SoundUtils.playSound(player, this.whiffSound);

        Action action = Action.builder().scheduledTask(impactTick, this::impact).build();
        AttackUtils.startAttackTimer(player, this.attackDuration);
        ActionUtils.performAction(player, action);
    }

    private void impact(PlayerEntity player, HeroActionEntity action) {
        MeleeUtils.forEach(player, action, (attacker, entity) -> {
            if (!(entity instanceof LivingEntity target)) return;
                MeleeUtils.basicHit(attacker, action, this.damage, this.knockback);
                SoundUtils.playSound(attacker, this.impactSound);
                onHitEvent.accept(attacker, target);
        });

        postAttackEvent.accept(player);
    }

    @Override
    public Identifier getAbilityId() {
        return abilityId;
    }
}
