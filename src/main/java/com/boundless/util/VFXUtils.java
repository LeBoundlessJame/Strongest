package com.boundless.util;

import com.boundless.BoundlessAPI;
import mod.chloeprime.aaaparticles.api.client.effekseer.ParticleEmitter;
import mod.chloeprime.aaaparticles.client.registry.EffectRegistry;

import java.util.Optional;

public class VFXUtils {
    /**
     * @param effectName the name of the effek file
     * @param id the
    */
    public void destroyEffectInstance(String effectName, int id) {
        var effect = EffectRegistry.get(BoundlessAPI.identifier(effectName));
        if (effect == null) return;
        Optional<ParticleEmitter> emitter = effect.getNamedEmitter(ParticleEmitter.Type.WORLD, BoundlessAPI.identifier(effectName + id));
        emitter.ifPresent(ParticleEmitter::stop);
    }
}
