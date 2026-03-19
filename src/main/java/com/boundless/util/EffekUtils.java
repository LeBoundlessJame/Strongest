package com.boundless.util;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class EffekUtils {


    //            EffekUtils.playRotatedEffect(BoundlessAPI.identifier("stun"), player, player.getPos().add(0, player.getHeight(), 0), new Vec3d(0.5f, 0.5f, 0.5f), Vec3d.ZERO);

    public static ParticleEmitterInfo playEffect(Identifier identifier, Entity user, Vec3d pos, Vec3d scale) {
        ParticleEmitterInfo instance = new ParticleEmitterInfo(identifier)
                .clone()
                .position(pos)
                .scale((float) scale.x, (float) scale.y, (float) scale.z);

        AAALevel.addParticle(user.getWorld(), true, instance);
        return instance;
    }

    public static ParticleEmitterInfo playEffect(Identifier identifier, Entity user, Vec3d pos, float scale) {
        return playEffect(identifier, user, pos, new Vec3d(scale, scale, scale));
    }

    public static ParticleEmitterInfo playRotatedEffect(Identifier identifier, Entity user, Vec3d pos, Vec3d scale, Vec3d rotation) {
        ParticleEmitterInfo instance = new ParticleEmitterInfo(identifier)
                .clone()
                .position(pos)
                .scale((float) scale.x, (float) scale.y, (float) scale.z)
                .rotation((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));

        AAALevel.addParticle(user.getWorld(), true, instance);
        return instance;
    }

    public static ParticleEmitterInfo playRandomRotatedEffect(Identifier identifier, Entity user, Vec3d pos, Vec3d scale) {
       return playRotatedEffect(identifier, user, pos, scale, new Vec3d(user.getRandom().nextFloat() * 360, user.getRandom().nextFloat() * 360, user.getRandom().nextFloat() * 360));
    }

    public static ParticleEmitterInfo playBoundRotatedEffect(Identifier identifier, Entity user, Vec3d scale, Vec3d rotation) {
        ParticleEmitterInfo instance = new ParticleEmitterInfo(identifier)
                .clone()
                .bindOnEntity(user)
                .scale((float) scale.x, (float) scale.y, (float) scale.z)
                .rotation((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));

        AAALevel.addParticle(user.getWorld(), true, instance);
        return instance;
    }


    public static ParticleEmitterInfo playBoundEffect(Identifier identifier, Entity user, Vec3d scale, Vec3d rotation) {
        ParticleEmitterInfo instance = new ParticleEmitterInfo(identifier)
                .clone()
                .scale((float) scale.x, (float) scale.y, (float) scale.z)
                .bindOnEntity(user);
        
        AAALevel.addParticle(user.getWorld(), true, instance);
        return instance;
    }

    public static void playVisual(LivingEntity entity, Identifier impactVisual) {
        Vec3d effectScale =  new Vec3d(entity.getScale() * 0.5f, entity.getScale() * 0.5f, entity.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(entity.getPitch(), entity.getYaw() * -1, 0);
        EffekUtils.playRotatedEffect(impactVisual, entity, entity.getPos().add(0, entity.getHeight() / 2, 0), effectScale, effectRotation);
    }
}