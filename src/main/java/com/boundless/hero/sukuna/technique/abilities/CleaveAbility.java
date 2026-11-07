package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.hero.sukuna.technique.components.SukunaComponents;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class CleaveAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("cleave"));
        List<LivingEntity> targets = new MeleeHitbox(5.0, 180.0).getTargetsInArc(player);

        for (LivingEntity target: targets) {
            target.damage(target.getDamageSources().playerAttack(player), getCleaveDamage(player, target));
        }

        if (!targets.isEmpty()) {
            SoundUtils.playSound(targets.getFirst(), SoundRegistry.SLASH_1);

            CameraUtils.playCameraShake(player);
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, 4, 4, true, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 6, 4, true, false, false));

            LivingEntity livingEntity = targets.getFirst();
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 4, true, false, false));

            EffekUtils.playRotatedEffect(BoundlessAPI.id("cleave_spikes"), livingEntity, new Vec3d(livingEntity.getX() - player.getRotationVector().x, livingEntity.getBodyY(0.5), livingEntity.getZ() - player.getRotationVector().z), new Vec3d(livingEntity.getHeight() / 10, livingEntity.getHeight() / 10, livingEntity.getHeight() / 10), new Vec3d(0, 0, player.getRotationVector().z));
            EffekUtils.playRotatedEffect(BoundlessAPI.id("upgraded_cleave"), livingEntity, new Vec3d(livingEntity.getX() - player.getRotationVector().x, livingEntity.getBodyY(0.5), livingEntity.getZ() - player.getRotationVector().z), new Vec3d(livingEntity.getHeight() / 10, livingEntity.getHeight() / 10, livingEntity.getHeight() / 10), new Vec3d(0, 0, player.getRotationVector().z));
            EffekUtils.playEffect(BoundlessAPI.id("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
        }

        AttackUtils.startAttackTimer(player, 20);
    }

    // % of max health, or flat damage, depending on which is higher
    public float getCleaveDamage(PlayerEntity player, LivingEntity target) {
        int fingersConsumed = ComponentUtils.getOr(player, SukunaComponents.SUKUNA_FINGERS_CONSUMED, 1);
        float percentage = MathHelper.lerp(fingersConsumed / 15, 20, 70);
        float damage = target.getMaxHealth() / percentage;
        // Todo: add as config
        damage = Math.max(damage, 200);
        return damage;
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 100;
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 2000;
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("cleave");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Cleave");
    }
}
