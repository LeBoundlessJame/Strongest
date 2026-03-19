package com.boundless.util;

import com.boundless.BoundlessAPI;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.api.common.AAALevel;
import mod.chloeprime.aaaparticles.api.common.ParticleEmitterInfo;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;
import net.minecraft.entity.Entity;

import java.util.Optional;

public class VFXUtils {
    /** Creates a particle emitter instance with a unique ID
     * @param user the effect uses this entity's ID to generate a unique ID for the emitter
     */
    public ParticleEmitterInfo createEffectInstance(Entity user, String effectName) {
        ParticleEmitterInfo particleEmitter = ParticleEmitterInfo.create(user.getWorld(), BoundlessAPI.identifier(effectName), BoundlessAPI.identifier(effectName + user.getId()));
        particleEmitter.position(pos);
        particleEmitter.scale(scale);
        return particleEmitter;
    }

    /**
     * @param particleEmitter the specific particle emitter instance (including id)
     */
    public void spawnEffectInstance(Entity user, ParticleEmitterInfo particleEmitter) {
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
