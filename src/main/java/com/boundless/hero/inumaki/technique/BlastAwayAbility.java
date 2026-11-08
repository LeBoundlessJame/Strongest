package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CameraUtils;
import com.boundless.util.CombatUtils;
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

public class BlastAwayAbility extends CursedSpeechAbility {
    @Override
    public void cursedSpeechEffect(PlayerEntity player, LivingEntity target) {
        Vec3d knockbackMultiplier = new Vec3d(24, 2, 24);
        // Todo: add status effect to targets as well later with packets
        target.setVelocity(player.getRotationVector().add(0, 0.5f, 0).multiply(knockbackMultiplier));
        target.velocityModified = true;
    }

    @Override
    public void onSpeechTrigger(PlayerEntity player, List<LivingEntity> targets) {
        TickScheduler.schedule(player.getWorld(), 4, () -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT, 4, 4, true, false, false));
        });
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
