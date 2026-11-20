package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public abstract class CursedSpeechAbility extends TechniqueAbility {
    public abstract void cursedSpeechEffect(LivingEntity user, LivingEntity target);
    public void onSpeechTrigger(LivingEntity user, List<LivingEntity> targets) {};

    int delayTicks = 5;

    @Override
    public void activate(LivingEntity user) {
        this.playFX(user);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(user);

        if (targets.isEmpty()) return;

        this.onSpeechTrigger(user, targets);

        TickScheduler.schedule(user.getWorld(), delayTicks, () -> {
            for (LivingEntity target : targets) {
                this.cursedSpeechEffect(user, target);
                this.takeRecoilDamage(user, target);
            }
        });
    }

    public void takeRecoilDamage(LivingEntity user, LivingEntity target) {
        float recoil = getRecoilDamage(user, target);

        if (user.hasStatusEffect(StatusEffectRegistry.COUGH_SYRUP)) {
            recoil /= 2;
        }

        if (recoil > 0.0f) {
            user.damage(DamageTypeRegistry.getDamageSource(user, DamageTypeRegistry.BLEED), recoil);
        }

        if (recoil >= 50f) {
            EffekUtils.playVisual(user, BoundlessAPI.id("dismantle_impact"), new Vec3d(user.getHeight() / 8, user.getHeight() / 8, user.getHeight() / 8));
        }
    }

    public void playFX(LivingEntity user) {
        SoundUtils.playSound(user, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 15, 22);
        Vec3d effectScale = new Vec3d(user.getScale() * 0.4f, user.getScale() * 0.4f, user.getScale() * 0.4f);
        Vec3d effectRotation = new Vec3d(user.getPitch(), user.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(user.getRotationVector().x, user.getStandingEyeHeight(), user.getRotationVector().z);

        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), user, effectScale, effectRotation, effectPosition);
    }

    public float getRecoilDamage(LivingEntity user, LivingEntity target) {
        if (!(target instanceof LivingEntity userTarget)) return 0.0f;
        float powerDifference = MathHelper.clamp(CursedEnergyManager.getCursedEnergy(user) / (float) CursedEnergyManager.getCursedEnergy(userTarget), 0, 1);
        // Todo: add min and max recoil to config
        return MathHelper.lerp(powerDifference, 275, 0);
    }
}
