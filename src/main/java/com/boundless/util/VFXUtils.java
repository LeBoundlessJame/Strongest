package com.boundless.util;

import com.boundless.BoundlessAPI;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.entity.Entity;

import java.util.Optional;

public class VFXUtils {
    /** Creates (and spawns) an effect instance
     * @param effectName the name of the effek file
     * @param id can be an entity ID for example; something unique to identify "this" version of the particle
     */
    public void createEffectInstance(Entity user, String effectName, int id) {
        ParticleEmitterInfo particleEmitter = ParticleEmitterInfo.create(user.getWorld(), BoundlessAPI.identifier(effectName), BoundlessAPI.identifier(effectName + id));
        /*
        particleEmitter.position(pos);
        particleEmitter.scale(scale);
         */
        AAALevel.addParticle(user.getWorld(), true, particleEmitter);
    }

    /**
     * @param effectName the name of the effek file
     * @param id can be an entity ID for example; something unique to identify "this" version of the particle
    */
    public void destroyEffectInstance(String effectName, int id) {
        var effect = EffectRegistry.get(BoundlessAPI.identifier(effectName));
        if (effect == null) return;
        Optional<ParticleEmitter> emitter = effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessAPI.identifier(effectName + id));
        emitter.ifPresent(ParticleEmitter::stop);
    }
}
