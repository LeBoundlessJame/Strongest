package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.AnimationManager;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class MalevolentShrineAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        if (user.getWorld().isClient) return;

        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 2, false, false, false));
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("domain_expansion_shrine"), 1.0f, true, false, 4000);
        SoundUtils.playSound(user, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);

        MalevolentShrineEntity shrine = new MalevolentShrineEntity(user, user.getWorld());
        shrine.setPosition(new Vec3d(user.getX() - user.getRotationVector().multiply(6).x, user.getY(), user.getZ() - user.getRotationVector().multiply(6).z));
        shrine.setPitch(user.getPitch());
        shrine.setYaw(user.getYaw());
        shrine.setOwner(user);
        user.getWorld().spawnEntity(shrine);

        SoundUtils.playSound(user, SoundRegistry.ROCK_CRUMBLING);

        for (LivingEntity userEntity: user.getWorld().getEntitiesByClass(LivingEntity.class, user.getBoundingBox().expand(shrine.domainRadius.getX(), shrine.domainRadius.getY(), shrine.domainRadius.getZ()), entity -> true)) {
            userEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.GRAYSCALE, 20, 0, false, false, false));
            userEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SHRINE_EFFECT, 20 + 20, 0, false, false, false));
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("malevolent_shrine");
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 100;
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Domain Expansion - Malevolent Shrine");
    }
}
