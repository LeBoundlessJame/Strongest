package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.DamageTypeRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public abstract class CursedSpeechAbility extends TechniqueAbility {
    public abstract void cursedSpeechEffect(PlayerEntity player, LivingEntity target);
    public void onSpeechTrigger(PlayerEntity player, List<LivingEntity> targets) {};

    int delayTicks = 5;

    @Override
    public void activate(PlayerEntity player) {
        this.playFX(player);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(player);

        if (targets.isEmpty()) return;

        this.onSpeechTrigger(player, targets);

        TickScheduler.schedule(player.getWorld(), delayTicks, () -> {
            for (LivingEntity target : targets) {
                this.cursedSpeechEffect(player, target);
                this.takeRecoilDamage(player, target);
            }
        });
    }

    public void takeRecoilDamage(PlayerEntity player, LivingEntity target) {
        float recoil = getRecoilDamage(player, target);
        if (recoil > 0.0f) {
            player.damage(DamageTypeRegistry.getDamageSource(player, DamageTypeRegistry.BLEED), recoil);
        }
    }

    public void playFX(PlayerEntity player) {
        SoundUtils.playSound(player, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 15, 22);
        Vec3d effectScale = new Vec3d(player.getScale() * 0.4f, player.getScale() * 0.4f, player.getScale() * 0.4f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(player.getRotationVector().x, player.getStandingEyeHeight(), player.getRotationVector().z);

        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), player, effectScale, effectRotation, effectPosition);
    }

    public float getRecoilDamage(PlayerEntity player, LivingEntity target) {
        if (!(target instanceof PlayerEntity playerTarget)) return 0.0f;
        float powerDifference = MathHelper.clamp(CursedEnergyManager.getCursedEnergy(player) / CursedEnergyManager.getMaxCursedEnergy(playerTarget), 0, 1);
        // Todo: add min and max recoil to config
        return MathHelper.lerp(powerDifference, 275, 0);
    }
}
