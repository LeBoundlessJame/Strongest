package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.open.OpenEntity;
import com.boundless.tick.TickScheduler;
import com.boundless.util.EffekUtils;
import com.boundless.util.PlayerAnimationUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class OpenAbility extends TechniqueAbility {

    // Todo: should probably let enemies know if fuga is about to happen
    @Override
    public void activate(PlayerEntity player) {
        EffekUtils.playEffect(BoundlessAPI.id("fuga_aura"), player, player.getPos().add(0, player.getHeight() / 2, 0), new Vec3d(0.2, 0.2, 0.2));
        //String message = "§6§l§ka§6" + " §6§l''Open.'' " + "§6§l§ka§6";

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 3, true, false, false));
        SoundUtils.playSound(player, SoundEvents.BLOCK_FIRE_AMBIENT);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("open"), true, 5000);

        for (int i = 0; i < 20; i++) {
            player.getWorld().addImportantParticle(ParticleTypes.LAVA, player.getX() + Math.cos(i), player.getY(), player.getZ() + Math.sin(i), 0, 0, 0);
        }

        TickScheduler.schedule(player.getWorld(), 90, () -> {
            OpenEntity openEntity = new OpenEntity(player, player.getWorld());
            openEntity.setVelocity(player.getRotationVector().multiply(4));
            openEntity.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1, player.getRotationVector().multiply(2).z));
            openEntity.setNoGravity(true);
            openEntity.setPitch(player.getPitch());
            openEntity.setYaw(player.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("fuga_arrow"), openEntity, new Vec3d(2f, 2f, 2f), new Vec3d(player.getPitch(), player.getYaw() * -1, 0));
            player.getWorld().spawnEntity(openEntity);
            //performFurnaceNukeIfPossible(player);
        });
    }

    public long getCooldown(PlayerEntity player) {
        return 20;
    }

    public int getCost(PlayerEntity player) {
        return 2000;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Open");
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("open");
    }
}
