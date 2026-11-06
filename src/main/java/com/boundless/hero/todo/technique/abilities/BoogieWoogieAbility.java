package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.TechniqueAbility;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.EffekUtils;
import com.boundless.util.PlayerAnimationUtils;
import com.boundless.util.RaycastUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class BoogieWoogieAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        clap(player);
        swapEntities(player, target);
    }

    private void swapEntities(Entity first, Entity second) {
        Vec3d firstPos = first.getPos();
        Vec3d secondPos = second.getPos();

        if (first instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.id("energy_spark"));
        }

        second.requestTeleport(firstPos.x, firstPos.y, firstPos.z);
        first.requestTeleport(secondPos.x, secondPos.y, secondPos.z);

        if (second instanceof LivingEntity livingEntity) {
            EffekUtils.playVisual(livingEntity, BoundlessAPI.id("energy_spark"));
            first.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, firstPos);
        }
    }

    public static void clap(PlayerEntity user) {
        if (user.getWorld().isClient()) return;
        PlayerAnimationUtils.playSyncedAnimation(user, BoundlessAPI.id("clap"), 2.0f, false, true, 3000);
        SoundUtils.playSound(user, SoundRegistry.CLAP_1, 8, 12);
    }

    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("boogie_woogie");
    }

    public int getCost(PlayerEntity player) {
        return 100;
    }

    public long getCooldown(PlayerEntity player) {
        return 10;
    }
}
