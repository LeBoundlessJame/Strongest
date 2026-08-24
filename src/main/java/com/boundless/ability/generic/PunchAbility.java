package com.boundless.ability.generic;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.action.Action;
import com.boundless.hero.black_sparks_hero.BlackFlashAbility;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import lombok.Builder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.boundless.hero.black_sparks_hero.BrawlerHero.DAMAGE;
import static com.boundless.registry.DataComponentRegistry.ATTACK_END;

@Builder
public class PunchAbility extends TechniqueAbility {
    private Identifier abilityId;

    @Builder.Default
    private int impactTick = 2;
    private int attackDuration;
    private float damage;

    @Builder.Default
    private Identifier animation = BoundlessAPI.identifier("hook");
    @Builder.Default
    private float animationSpeed = 1.0f;

    @Builder.Default
    private SoundEvent whiffSound = SoundRegistry.MISS_HIT;
    @Builder.Default
    private SoundEvent impactSound = SoundRegistry.IMPACT_HEAVY_1;

    @Builder.Default
    private Consumer<PlayerEntity> preHitEvent = (player) -> {};
    @Builder.Default
    private BiConsumer<PlayerEntity, LivingEntity> onHitEvent = (player, target) -> {};
    @Builder.Default
    private Consumer<PlayerEntity> postHitEvent = (player) -> {};

    @Override
    public void activate(PlayerEntity player) {
        if (!(player.getWorld().getTime() >= HeroUtils.getHeroStack(player).getOrDefault(ATTACK_END, 0L))) return;

        DataComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, attackCount % 2 == 0, true, 3000);

        SoundUtils.playSound(player, SoundRegistry.MISS_HIT);
        Action hook = Action.builder()
                .scheduledTask(4, (user, action) -> {
                    MeleeUtils.forEach(player, action, (attacker, entity) -> {
                        if (BlackFlashAbility.calculateBlackFlash(attacker)) {
                            // Todo: make it so that upwards knockback is optional
                            BlackFlashAbility.blackFlash(attacker, 80, new Vec3d(0.2f, 0.0f, 0.2f), action);
                            return;
                        }
                        MeleeUtils.basicHit(user, action, DAMAGE.lightAttack.get());
                    });
                })
                .build();

        AttackUtils.startAttackTimer(player, 4);
        ActionUtils.performAction(player, hook);
    }

    @Override
    public Identifier getAbilityId() {
        return abilityId;
    }
}
