package com.boundless.hero.sukuna.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class DismantleAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, user, 1);
        int attackCount = HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        EntityHitResult result = RaycastUtils.raycast(user, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(user, 64, 1.5f) : result.getEntity();

        if (target == null || target == user) return;
        target.damage(target.getDamageSources().mobAttack(user), this.getDamage(user));

        SoundUtils.playSound(user, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 10, 11);
        EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("upgraded_dismantle"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(1, 1, 1));
        EffekUtils.playEffect(BoundlessAPI.id("dismantle_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), target.getHeight() / 16);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("dismantle");
    }

    public float getDamage(LivingEntity user) {
        return 20;
    }

    public int getCost(LivingEntity user) {
        return 200;
    }

    public long getCooldown(LivingEntity user) {
        return 10;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Dismantle");
    }
}
