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
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ExplodeAbility extends CursedSpeechAbility {
    @Override
    public void onSpeechTrigger(LivingEntity user, List<LivingEntity> targets) {
        // TODO: FIX ASAP CameraUtils.playCameraShake(user);

        TickScheduler.schedule(user.getWorld(), 3, () -> {
            SoundUtils.playSound(targets.getFirst(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE);
            SoundUtils.playSound(targets.getFirst(), SoundEvents.ENTITY_GENERIC_EXPLODE.value());
        });
    }

    @Override
    public void cursedSpeechEffect(LivingEntity user, LivingEntity target) {
        target.damage(target.getDamageSources().mobAttack(user), 175);
        EffekUtils.playEffect(BoundlessAPI.id("explode"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(target.getScale() * 0.3f, target.getScale() * 0.3f, target.getScale() * 0.3f));
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 600;
    }

    @Override
    public int getCost(LivingEntity user) {
        return 1500;
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("explode");
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Say 'Explode'");
    }
}
