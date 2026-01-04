package com.boundless.util;

import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class EffekUtils {

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
}