package com.boundless.hero.todo.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.EffekUtils;
import com.boundless.util.AnimationManager;
import com.boundless.util.RaycastUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class BoogieWoogieAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        EntityHitResult result = RaycastUtils.raycast(user, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(user, 64, 1.5f) : result.getEntity();

        if (target == null || target == user) return;
        clap(user);
        swapEntities(user, target);
    }

    public void swapEntities(Entity first, Entity second) {
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

    public void clap(LivingEntity user) {
        if (user.getWorld().isClient()) return;
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("clap"), 2.0f, false, true, 3000);
        SoundUtils.playSound(user, SoundRegistry.CLAP_1, 8, 12);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("boogie_woogie");
    }

    public int getCost(LivingEntity user) {
        return 100;
    }

    public long getCooldown(LivingEntity user) {
        return 10;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Boogie Woogie");
    }
}
