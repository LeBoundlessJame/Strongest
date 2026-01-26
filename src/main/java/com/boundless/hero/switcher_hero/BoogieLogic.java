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

import java.util.HashMap;
import java.util.function.BiConsumer;

public class BoogieLogic {

    public static HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> BOOGIE_MAP = getBoogieMap();

    public static HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> getBoogieMap() {
        HashMap<String, BiConsumer<PlayerEntity, HeroActionEntity>> boogieMap = new HashMap<>();
        boogieMap.put("standard", BoogieLogic::standardSwap);
        boogieMap.put("swapWithPrimary", BoogieLogic::swapWithPrimary);
        boogieMap.put("swapWithSecondary", BoogieLogic::swapWithSecondary);
        boogieMap.put("feint", BoogieLogic::feint);
        boogieMap.put("swapTwo", BoogieLogic::swapTwo);
        return boogieMap;
    }

    public static void swapTwo(PlayerEntity player, HeroActionEntity heroAction) {
        Integer primary = HeroUtils.getHeroStack(player).get(SwitcherHero.PRIMARY_TARGET_ID);
        Integer secondary = HeroUtils.getHeroStack(player).get(SwitcherHero.SECONDARY_TARGET_ID);

        if (primary == null || secondary == null) return;
        Entity primaryTarget = player.getWorld().getEntityById(primary);
        Entity secondaryTarget = player.getWorld().getEntityById(secondary);
        if (primaryTarget == null || secondaryTarget == null) return;

        swapEntities(primaryTarget, secondaryTarget);
    }

    public static void feint(PlayerEntity player, HeroActionEntity heroAction) {}

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
        swapEntities(player, target);
    }

    public static void swapWithPrimary(PlayerEntity player, HeroActionEntity heroAction) {
        swapWithTarget(player, "primary");
    }

    public static void swapWithSecondary(PlayerEntity player, HeroActionEntity heroAction) {
        swapWithTarget(player, "secondary");
    }

    public static void standardSwap(PlayerEntity player, HeroActionEntity heroAction) {
        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        BoogieLogic.swapEntities(player, target);
    }

    public static void clap(PlayerEntity user) {
        if (user.getWorld().isClient()) return;

        TargetSelectMenu.closeMenu(user);

        AnimationUtils.playSyncedAnimation(user, BoundlessAPI.identifier("clap"), 1.0f, false, true, 3000);
        HeroUtils.getHeroStack(user).set(SwitcherHero.CLAP_SELECT_TIME, user.getWorld().getTime() + 5L);
        HeroUtils.getHeroStack(user).set(SwitcherHero.BOOGIE_SELECTION, "standard");
        ActionUtils.performDelayedAction(user, BoogieLogic::boogie, 5);
    }

    public static void boogie(PlayerEntity player, HeroActionEntity heroAction) {
        String swapType = HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.BOOGIE_SELECTION, "standard");
        SoundUtils.playSound(player, SoundRegistry.CLAP_1, 8, 12);
        ActionUtils.performDelayedAction(player, BOOGIE_MAP.get(swapType), 0);
        HeroUtils.getHeroStack(player).set(SwitcherHero.BOOGIE_SELECTION, "standard");
        HeroUtils.getHeroStack(player).set(SwitcherHero.CLAP_SELECT_TIME, 0L);
        EnergyUtils.changeEnergyPercentage(player, -5f);
    }

    public static boolean isSelectingClap(PlayerEntity player) {
        return player.getWorld().getTime() <= HeroUtils.getHeroStack(player).getOrDefault(SwitcherHero.CLAP_SELECT_TIME, 0L);
    }
}
