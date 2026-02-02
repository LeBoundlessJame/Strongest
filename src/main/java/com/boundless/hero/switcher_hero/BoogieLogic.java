package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.entity.rock.RockEntity;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class BoogieLogic {
    public static void swapTwo(PlayerEntity player) {
        Integer primary = HeroUtils.getHeroStack(player).get(SwitcherHero.PRIMARY_TARGET_ID);
        Integer secondary = HeroUtils.getHeroStack(player).get(SwitcherHero.SECONDARY_TARGET_ID);

        if (primary == null || secondary == null) return;
        Entity primaryTarget = player.getWorld().getEntityById(primary);
        Entity secondaryTarget = player.getWorld().getEntityById(secondary);
        if (primaryTarget == null || secondaryTarget == null) return;

        clap(player);
        swapEntities(primaryTarget, secondaryTarget);
    }

    public static void swapEntities(Entity first, Entity second) {
        Vec3d firstPos = first.getPos();
        Vec3d secondPos = second.getPos();

        if (first instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.identifier("energy_spark"));
        }

        second.requestTeleport(firstPos.x, firstPos.y, firstPos.z);
        first.requestTeleport(secondPos.x, secondPos.y, secondPos.z);

        if (second instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.identifier("energy_spark"));
            first.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, firstPos);
        }

        if (second instanceof RockEntity rock && rock.getOwner() == first && rock.getOwner() instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 0, false, false, false));
            RockThrowLogic.blackFlash(player);
        }
    }

    public static void swapWithTarget(PlayerEntity player, String targetType) {
        Integer id = HeroUtils.getHeroStack(player).get(targetType.equals("primary") ? SwitcherHero.PRIMARY_TARGET_ID : SwitcherHero.SECONDARY_TARGET_ID);
        if (id == null) return;

        Entity target = player.getWorld().getEntityById(id);
        if (target == null) return;
        clap(player);
        swapEntities(player, target);
    }

    public static void swapWithPrimary(PlayerEntity player) {
        swapWithTarget(player, "primary");
    }

    public static void swapWithSecondary(PlayerEntity player) {
        swapWithTarget(player, "secondary");
    }

    public static void standardSwap(PlayerEntity player) {
        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        clap(player);
        BoogieLogic.swapEntities(player, target);
    }

    public static void clap(PlayerEntity user) {
        if (user.getWorld().isClient()) return;
        TargetSelectMenu.closeMenu(user);
        AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("clap"), 2.0f, false, true, 3000);
        SoundUtils.playSound(user, SoundRegistry.CLAP_1, 8, 12);
        EnergyUtils.changeEnergyPercentage(user, -5f);
    }

    public static boolean isSelectingClap(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.CLAP_SELECT_TIME, 0L);
    }

    public static void tick(PlayerEntity player) {
        if (player.isSneaking()) {
            HeroUtils.setLoadout(player, SwitcherHero.ABILITY_LOADOUTS.get("LOADOUT_2"));
        } else {
            HeroUtils.setLoadout(player, SwitcherHero.ABILITY_LOADOUTS.get("LOADOUT_1"));
        }
    }

}
