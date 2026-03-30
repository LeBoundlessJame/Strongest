package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiConsumer;

@Getter
@Setter
public class KnockbackCombo extends Combo {
    public float damage = 20f;
    public Vec3d knockback = new Vec3d(2.5f, 0.5f, 2.5f);
    public Identifier animation = BoundlessAPI.identifier("hook");
    public float animationSpeed = 1.0f;
    public int impactTick = 4;

    public KnockbackCombo(String sequence, String comboName) {
        super(sequence, comboName);
    }

    @Override
    public void executeCombo(PlayerEntity player) {
        AnimationUtils.playAlternatingSyncedAnimation(player, this.getAnimation(), this.getAnimationSpeed(), true, 3000);
        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.LIMITED_SPEED, ConfigRegistry.HERO_CONFIG.COMBAT_CONFIG.sprintSpeedLimitDuration.get(), 0, false, false, false));
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 8, 12);

        Action attack = ActionUtils.singleAction(4, this::comboLogic);
        ActionUtils.performAction(player, attack);
    }

    public void comboLogic(PlayerEntity player, HeroActionEntity action) {
        if (MeleeUtils.getTargets(player, action).isEmpty()) return;

        CameraUtils.playCameraShake(player);

        MeleeUtils.forEach(player, action, (user, entity) -> {
            if (!(entity instanceof LivingEntity livingEntity)) return;

            entity.damage(entity.getDamageSources().generic(), this.getDamage());
            CombatUtils.playImpactVisual(player, livingEntity, BoundlessAPI.identifier("melee_impact"));
            SoundUtils.playSound(player, SoundRegistry.IMPACT_HEAVY_1);
            SoundUtils.playSound(player, player.getRandom().nextBoolean() ? SoundRegistry.PUNCH_1 : SoundRegistry.PUNCH_2, 9, 11);
            MeleeUtils.knockback(user, livingEntity, this.getKnockback());
        });
    }
}
