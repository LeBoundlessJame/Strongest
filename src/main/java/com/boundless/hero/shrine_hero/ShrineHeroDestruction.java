package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.SimpleDomain;
import com.boundless.action.Action;
import com.boundless.entity.malevolent_shrine.MalevolentShrineEntity;
import com.boundless.entity.open.OpenEntity;
import com.boundless.registry.GameRulesRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.StrongestComponents;
import com.boundless.util.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ShrineHeroDestruction {
    public static Ability OPEN = AbilityUtils.ability(ShrineHeroDestruction::open, ShrineHero.COOLDOWNS.open.get(), BoundlessAPI.identifier("open"), "Open", ShrineHero.METER_CONFIG.openCost.get());
    public static Ability SHRINE = AbilityUtils.ability(ShrineHeroDestruction::shrine, ShrineHero.COOLDOWNS.domainExpansion.get(), BoundlessAPI.identifier("malevolent_shrine"), "Malevolent Shrine", ShrineHero.METER_CONFIG.domainExpansionCost.get());

    public static void open(PlayerEntity player) {
        EffekUtils.playEffect(BoundlessAPI.identifier("fuga_aura"), player, player.getPos().add(0, player.getHeight() / 2, 0), new Vec3d(0.2, 0.2, 0.2));
        String message = "§6§l§ka§6" + " §6§l''Open.'' " + "§6§l§ka§6";

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 3, true, false, false));
        SoundUtils.playSound(player, SoundEvents.BLOCK_FIRE_AMBIENT);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("open"), true, 5000);

        for (int i = 0; i < 20; i++) {
            player.getWorld().addImportantParticle(ParticleTypes.LAVA, player.getX() + Math.cos(i), player.getY(), player.getZ() + Math.sin(i), 0, 0, 0);
        }

        Action shootOpen = ActionUtils.singleAction(90, (user, heroAction) -> {
            OpenEntity openEntity = new OpenEntity(user, user.getWorld());
            openEntity.setVelocity(user.getRotationVector().multiply(4));
            openEntity.setPosition(user.getPos().add(user.getRotationVector().multiply(2).x, 1, user.getRotationVector().multiply(2).z));
            openEntity.setNoGravity(true);
            openEntity.setPitch(user.getPitch());
            openEntity.setYaw(user.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.identifier("fuga_arrow"), openEntity, new Vec3d(2f, 2f, 2f), new Vec3d(user.getPitch(), user.getYaw() * -1, 0));
            user.getWorld().spawnEntity(openEntity);
            performFurnaceNukeIfPossible(user);
        });

        for (PlayerEntity playerEntity : player.getWorld().getEntitiesByClass(PlayerEntity.class, player.getBoundingBox().expand(32, 16, 32), entity -> true)) {
            playerEntity.sendMessage(Text.of(playerEntity != player ? message : message.replace("'", "")), true);
        }

        ActionUtils.performAction(player, shootOpen);
    }

    // Todo: make literally all of this better
    public static void shrine(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, ShrineHero.DOMAIN.initialDelay.get(), 2, false, false, false));
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("domain_expansion_shrine"), 1.0f, true, false, 4000);
        SoundUtils.playSound(player, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK);

        MalevolentShrineEntity shrine = new MalevolentShrineEntity(player, player.getWorld());
        shrine.setPosition(new Vec3d(player.getX() - player.getRotationVector().multiply(6).x, player.getY(), player.getZ() - player.getRotationVector().multiply(6).z));
        shrine.setPitch(player.getPitch());
        shrine.setYaw(player.getYaw());
        shrine.setOwner(player);
        player.getWorld().spawnEntity(shrine);

        SoundUtils.playSound(player, SoundRegistry.ROCK_CRUMBLING);

        for (PlayerEntity playerEntity: player.getWorld().getEntitiesByClass(PlayerEntity.class, player.getBoundingBox().expand(shrine.domainRadius.getX(), shrine.domainRadius.getY(), shrine.domainRadius.getZ()), entity -> true)) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.GRAYSCALE, ShrineHero.DOMAIN.initialDelay.get(), 0, false, false, false));
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.SHRINE_EFFECT, ShrineHero.DOMAIN.initialDelay.get() + 20, 0, false, false, false));
        }
    }

    // Todo: Find a nicer way to incorporate simple domain later.
    public static void performFurnaceNukeIfPossible(PlayerEntity player) {
        List<MalevolentShrineEntity> entries = player.getWorld().getEntitiesByClass(MalevolentShrineEntity.class, player.getBoundingBox().expand(200, 200, 200), entity -> entity.getOwner() == player);
        if (entries.isEmpty()) return;
        MalevolentShrineEntity shrine = entries.getFirst();
        if (shrine == null) return;

        shrine.initiateFurnaceNuke();
        EffekUtils.playEffect(BoundlessAPI.identifier("domain_fuga"), shrine, shrine.getPos().add(0f, 0.1f, 0f), 10);

        shrine.entitiesInRange.forEach(entity -> {
            if (!shrine.getWorld().isClient && shrine.getWorld().getGameRules().getBoolean(GameRulesRegistry.TECHNIQUE_DESTRUCTION)) {
                player.getWorld().createExplosion(shrine, entity.getX(), entity.getY(), entity.getZ(), 10f, true, World.ExplosionSourceType.BLOCK);
            }

            if (SimpleDomain.isSimpleDomainActive(player)) {
                ComponentUtils.incrementFloat(StrongestComponents.SIMPLE_DOMAIN_HEALTH, player, ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestFurnaceArrowDamage.get(), ShrineHero.DAMAGE.strongestFurnaceArrowDamage.get()), 0, 150);
                return;
            }

            entity.timeUntilRegen = 0;
            entity.damage(entity.getDamageSources().generic(), ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestFurnaceArrowDamage.get(), ShrineHero.DAMAGE.strongestFurnaceArrowDamage.get()));
        });
    }
}
