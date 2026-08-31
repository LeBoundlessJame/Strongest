package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.action.Action;
import com.boundless.combat.HitEffects;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
    private int attackDuration = 10;

    private float damage;

    private Identifier animation = BoundlessAPI.id("hook");
    private float animationSpeed = 1.0f;

    private SoundEvent whiffSound = SoundRegistry.MISS_HIT;
    private HitEffects impactEffects = new HitEffects(List.of(BoundlessAPI.id("melee_impact")), List.of(SoundRegistry.IMPACT_HEAVY_1));

    private Consumer<PlayerEntity> preAttackEvent = (player) -> {
    };
    private BiConsumer<PlayerEntity, LivingEntity> onHitEvent = (player, target) -> {
    };
    private Consumer<PlayerEntity> postAttackEvent = (player) -> {
    };

    private Vec3d knockback = new Vec3d(0.6, 0.3, 0.6);

    private BiConsumer<PlayerEntity, HeroActionEntity> impactEventOverride;

    private Function<PlayerEntity, Boolean> mirrorAnimationProvider = (player) -> {
        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        return attackCount % 2 == 0;
    };

    @Override
    public void activate(PlayerEntity player) {
        if (!(player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L))) return;

        preAttackEvent.accept(player);
        PlayerAnimationUtils.playSyncedAnimation(player, this.animation, this.animationSpeed, mirrorAnimationProvider.apply(player), true, 3000);
        SoundUtils.playSound(player, this.whiffSound);

        Action action = Action.builder().scheduledTask(impactTick, this::impact).build();
        AttackUtils.startAttackTimer(player, this.attackDuration);
        ActionUtils.performAction(player, action);
    }

    private void impact(PlayerEntity player, HeroActionEntity action) {
        if (impactEventOverride != null) {
            impactEventOverride.accept(player, action);
            postAttackEvent.accept(player);
            return;
        }

        CombatUtils.hit(player, action, damage, knockback, onHitEvent, impactEffects);

        postAttackEvent.accept(player);
    }

    @Override
    public Identifier getAbilityId() {
        return abilityId;
    }
}
