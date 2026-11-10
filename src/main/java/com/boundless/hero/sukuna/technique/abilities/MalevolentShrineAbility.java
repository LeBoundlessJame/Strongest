package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.PlayerAnimationUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class MalevolentShrineAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 2, false, false, false));
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("domain_expansion_shrine"), 1.0f, true, false, 4000);
        SoundUtils.playSound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);

        MalevolentShrineEntity shrine = new MalevolentShrineEntity(player, player.getWorld());
        shrine.setPosition(new Vec3d(player.getX() - player.getRotationVector().multiply(6).x, player.getY(), player.getZ() - player.getRotationVector().multiply(6).z));
        shrine.setPitch(player.getPitch());
        shrine.setYaw(player.getYaw());
        shrine.setOwner(player);
        player.getWorld().spawnEntity(shrine);

        SoundUtils.playSound(player, SoundRegistry.ROCK_CRUMBLING);

        for (PlayerEntity playerEntity: player.getWorld().getEntitiesByClass(PlayerEntity.class, player.getBoundingBox().expand(shrine.domainRadius.getX(), shrine.domainRadius.getY(), shrine.domainRadius.getZ()), entity -> true)) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.GRAYSCALE, 20, 0, false, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SHRINE_EFFECT, 20 + 20, 0, false, false, false));
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("malevolent_shrine");
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 100;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Domain Expansion - Malevolent Shrine");
    }
}
