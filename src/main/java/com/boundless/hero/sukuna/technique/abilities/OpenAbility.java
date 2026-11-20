package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.open.OpenEntity;
import com.boundless.tick.TickScheduler;
import com.boundless.util.EffekUtils;
import com.boundless.util.AnimationManager;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class OpenAbility extends TechniqueAbility {

    // Todo: should probably let enemies know if fuga is about to happen
    @Override
    public void activate(LivingEntity user) {
        EffekUtils.playEffect(BoundlessAPI.id("fuga_aura"), user, user.getPos().add(0, user.getHeight() / 2, 0), new Vec3d(0.2, 0.2, 0.2));
        //String message = "§6§l§ka§6" + " §6§l''Open.'' " + "§6§l§ka§6";

        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 3, true, false, false));
        SoundUtils.playSound(user, SoundEvents.BLOCK_FIRE_AMBIENT);
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("open"), true, 5000);

        for (int i = 0; i < 20; i++) {
            user.getWorld().addImportantParticle(ParticleTypes.LAVA, user.getX() + Math.cos(i), user.getY(), user.getZ() + Math.sin(i), 0, 0, 0);
        }

        TickScheduler.schedule(user.getWorld(), 90, () -> {
            OpenEntity openEntity = new OpenEntity(user, user.getWorld());
            openEntity.setVelocity(user.getRotationVector().multiply(4));
            openEntity.setPosition(user.getPos().add(user.getRotationVector().multiply(2).x, 1, user.getRotationVector().multiply(2).z));
            openEntity.setNoGravity(true);
            openEntity.setPitch(user.getPitch());
            openEntity.setYaw(user.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("fuga_arrow"), openEntity, new Vec3d(2f, 2f, 2f), new Vec3d(user.getPitch(), user.getYaw() * -1, 0));
            user.getWorld().spawnEntity(openEntity);
            //performFurnaceNukeIfPossible(user);
        });
    }

    public long getCooldown(LivingEntity user) {
        return 20;
    }

    public int getCost(LivingEntity user) {
        return 2000;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Divine Flame - Open");
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("open");
    }
}
