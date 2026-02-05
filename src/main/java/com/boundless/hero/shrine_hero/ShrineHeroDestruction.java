package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.entity.open.OpenEntity;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;

import static com.boundless.hero.black_sparks_hero.BlackSparksHero.COOLDOWNS;

public class ShrineHeroDestruction {
    public static Ability OPEN = AbilityUtils.ability(ShrineHeroDestruction::open, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("open"), "Open");
    public static Ability SHRINE = AbilityUtils.ability(ShrineHeroDestruction::shrine, COOLDOWNS.lightAttack.get(), BoundlessAPI.identifier("malevolent_shrine"), "Malevolent Shrine");

    public static void open(PlayerEntity player) {
        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_aura"), player, player.getPos().add(0, player.getHeight() / 2, 0), new Vec3d(0.2, 0.2, 0.2));

        String message = "§6§l§ka§6" + " §6§l''Open.'' " + "§6§l§ka§6";

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 45, 3, true, false, false));
        SoundUtils.playSound(player, SoundEvents.BLOCK_FIRE_AMBIENT);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("open"), true, 5000);

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

    // Todo: make literally all of this better
    public static void shrine(PlayerEntity player) {
        String message = "§c§l§ka§c §c§l''Ryoiki Tenkai''. §c§l§ka§c";

        int domainExpansionDuration = 400;
        float domainRadius = 100;
        float domainTickDamage = 1;

        BlockPos domainOrigin = player.getBlockPos();

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 3, false, false, false));
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("domain_expansion_shrine"), 1.0f, true, false, 4000);
        SoundUtils.playSound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);

        Vec3d shrinePosition = new Vec3d(player.getX() - player.getRotationVector().multiply(5).x, player.getY(), player.getZ() - player.getRotationVector().multiply(5).z);
        MalevolentShrineEntity shrine = new MalevolentShrineEntity(player, player.getWorld());
        shrine.setPosition(shrinePosition);
        shrine.setPitch(player.getPitch());
        shrine.setYaw(player.getYaw());
        shrine.setMaxLifetime(domainExpansionDuration);
        player.getWorld().spawnEntity(shrine);

        EffekUtils.playEffect(BoundlessAPI.identifier("surehit_rendition"), player, player.getPos().add(0f, 0.1f, 0f).add(player.getRotationVector().normalize().multiply(10)), 5.0f);
        //EffekUtils.playEffect(BoundlessAPI.identifier("shrine_visuals"), player, player.getPos().add(0f, 0.1f, 0f).add(player.getRotationVector().normalize().multiply(10)), 5.0f);

        /*
        for (LivingEntity livingEntity: player.getWorld().getEntitiesByClass(LivingEntity.class, new Box(domainOrigin).expand(100, 50, 100), entity -> true)) {
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.IMPACT_FRAME_EFFECT, 400, 0, false, false, false));
        }

         */

        BiConsumer<PlayerEntity, HeroActionEntity> sureHitTask = (playerEntity, heroAction) -> {
            if (playerEntity.age % 3 == 0) {
                //CameraUtils.playCameraShake(playerEntity);

                if (player.getWorld() instanceof ServerWorld) {
                    int environmentQuality = 8;
                    for (int x = 0; x < environmentQuality; x++) {
                        for (int y = 0; y < environmentQuality / 2; y++) {
                            for (int z = 0; z < environmentQuality; z++) {

                                float xPos = MathHelper.nextFloat(player.getRandom(), domainOrigin.getX() - domainRadius, domainOrigin.getX() + domainRadius);
                                float yPos = MathHelper.nextFloat(player.getRandom(), domainOrigin.getY() - domainRadius, domainOrigin.getY() + domainRadius);
                                float zPos = MathHelper.nextFloat(player.getRandom(), domainOrigin.getZ() - domainRadius, domainOrigin.getZ() + domainRadius);

                                EffekUtils.playRandomRotatedEffect(BoundlessAPI.identifier("dismantle_star"), player, new Vec3d(xPos, yPos, zPos), new Vec3d(1, 1, 1));
                            }
                        }
                    }
                }
            }

            if (player.age % 3 == 0) {
                for (LivingEntity livingEntity : playerEntity.getWorld().getEntitiesByClass(LivingEntity.class, new Box(domainOrigin).expand(100, 50, 100), entity -> true)) {
                    if (livingEntity != playerEntity) {
                        livingEntity.damage(livingEntity.getDamageSources().magic(), domainTickDamage);
                        livingEntity.timeUntilRegen = 0;
                    }
                }

            }
        };
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> scheduledTasks;
        scheduledTasks = ActionUtils.repeatTask(sureHitTask, 50, domainExpansionDuration);
        ActionUtils.performAction(player, ActionUtils.action(scheduledTasks));
    }
}
