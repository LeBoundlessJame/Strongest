package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CameraUtils;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class BlastAwayAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        Vec3d effectScale =  new Vec3d(player.getScale() * 0.4f, player.getScale() * 0.4f, player.getScale() * 0.4f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(player.getRotationVector().x, player.getStandingEyeHeight(), player.getRotationVector().z);
        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), player, effectScale, effectRotation, effectPosition);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(player);

        CameraUtils.playCameraShake(player);

        if (!targets.isEmpty()) {
            TickScheduler.schedule(player.getWorld(), 5, () -> {
                Vec3d knockbackMultiplier = new Vec3d(24, 2, 24);

                for (LivingEntity target: targets) {
                    // Todo: add status effect to targets as well later with packets
                    player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 8, 4, true, false, false));
                    target.setVelocity(player.getRotationVector().add(0, 0.5f, 0).multiply(knockbackMultiplier));
                    target.velocityModified = true;
                }
            });
        }
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 2000;
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 2000;
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("blast_away");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Say 'Blast away'");
    }
}
