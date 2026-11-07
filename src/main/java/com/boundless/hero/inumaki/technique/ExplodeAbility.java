package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CameraUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ExplodeAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        SoundUtils.playSound(player, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 15, 22);

        Vec3d effectScale =  new Vec3d(player.getScale() * 0.4f, player.getScale() * 0.4f, player.getScale() * 0.4f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(player.getRotationVector().x, player.getStandingEyeHeight(), player.getRotationVector().z);
        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), player, effectScale, effectRotation, effectPosition);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(player);

        CameraUtils.playCameraShake(player);

        if (!targets.isEmpty()) {
            TickScheduler.schedule(player.getWorld(), 5, () -> {
                for (LivingEntity target: targets) {
                    // Todo: add to config
                    target.damage(target.getDamageSources().playerAttack(player), 175);
                    EffekUtils.playEffect(BoundlessAPI.id("explode"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(target.getScale() * 0.3f, target.getScale() * 0.3f, target.getScale() * 0.3f));
                }
            });
        }
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 800;
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 1500;
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("explode");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Say 'Explode'");
    }
}
