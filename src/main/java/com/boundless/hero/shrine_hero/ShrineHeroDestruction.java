package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.open.OpenEntity;
import com.boundless.util.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import static com.boundless.hero.black_sparks_hero.BlackSparksHero.COOLDOWNS;

public class ShrineHeroDestruction {
    public static Ability OPEN = AbilityUtils.ability(ShrineHeroDestruction::open, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("open"), "Open");

    public static void open(PlayerEntity player) {
        String message = "§6§l§ka§6" + " §6§l''Open.'' " + "§6§l§ka§6";

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 45, 3, true, false, false));
        SoundUtils.playSound(player, SoundEvents.BLOCK_FIRE_AMBIENT);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("open"));

        Action shootOpen = ActionUtils.singleAction(45, (user, heroAction) -> {
            OpenEntity openEntity = new OpenEntity(user, user.getWorld());
            openEntity.setVelocity(user.getRotationVector().multiply(4));
            openEntity.setPosition(user.getPos().add(user.getRotationVector().multiply(2).x, 1, user.getRotationVector().multiply(2).z));
            openEntity.setNoGravity(true);
            openEntity.setPitch(user.getPitch());
            openEntity.setYaw(user.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.identifier("fuga_arrow"), openEntity, new Vec3d(2f, 2f, 2f), new Vec3d(user.getPitch(), user.getYaw() * -1, 0));
            user.getWorld().spawnEntity(openEntity);
        });

        for (PlayerEntity playerEntity : player.getWorld().getEntitiesByClass(PlayerEntity.class, player.getBoundingBox().expand(32, 16, 32), entity -> true)) {
            playerEntity.sendMessage(Text.of(playerEntity != player ? message : message.replace("'", "")), true);
        }

        ActionUtils.performAction(player, shootOpen);
    }

}
