package com.boundless.hero.shrine_hero.ability;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.hero.shrine_hero.ShrineHelper;
import com.boundless.hero.shrine_hero.ShrineHero;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AnimationUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.RaycastUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class DismantleAbility extends Ability {
    // Todo: make configurable
    public static float DISMANTLE_RANGE = 64;

    public DismantleAbility(Identifier id) {
        super(id);
        this.setCooldown(20);
        this.setSkillSlot(2);
        this.setIcon(BoundlessAPI.hudPNG("dismantle"));
    }

    @Override
    public void executeAbility(PlayerEntity player) {
        AnimationUtils.playAlternatingSyncedAnimation(player, BoundlessAPI.identifier("dismantle_1"), 1.5f, true, 3000);
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 13, 16);

        EntityHitResult result = RaycastUtils.raycast(player, DISMANTLE_RANGE);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, DISMANTLE_RANGE, 1.5f) : result.getEntity();

        if (!(target instanceof LivingEntity livingEntity)) return;

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 10, 0, false, false, false));
        SoundUtils.playSound(player, SoundRegistry.HEAVY_CUT_3, 13, 16);
        livingEntity.timeUntilRegen = 0;
        livingEntity.damage(livingEntity.getDamageSources().generic(), ShrineHelper.getScaledDamage(player, ShrineHero.DAMAGE.weakestDismantle.get(), ShrineHero.DAMAGE.strongestDismantle.get()));

        float force = livingEntity.isOnGround() ? 1.2f : 2f;
        livingEntity.setVelocity(player.getRotationVector().x * force, 0, player.getRotationVector().z * force);
        livingEntity.velocityModified = true;
        SoundUtils.playSound(player, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 10, 11);
        EffekUtils.playRandomRotatedEffect(BoundlessAPI.identifier("upgraded_dismantle"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), new Vec3d(1, 1, 1));
        EffekUtils.playEffect(BoundlessAPI.identifier("dismantle_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), livingEntity.getHeight() / 16);
    }
}
