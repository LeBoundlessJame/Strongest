package com.boundless.hero.nobara.technique.abilities;


import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.hero.nobara.technique.components.ResonanceData;
import com.boundless.hero.nobara.technique.components.StrawdollComponents;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ResonanceAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        TickScheduler.schedule(player.getWorld(), 10, () -> {
            Map<UUID, ResonanceData> resonanceTracker = ComponentUtils.getOr(player, StrawdollComponents.RESONANCE_TRACKER, new HashMap<>());
            resonanceTracker = new HashMap<>(resonanceTracker);

            Iterator<Map.Entry<UUID, ResonanceData>> iterator = resonanceTracker.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<UUID, ResonanceData> entry = iterator.next();
                ResonanceData data = entry.getValue();

                if (data.isExpired(player.getWorld().getTime())) {
                    iterator.remove();
                } else if (data.isResonanceReady() && player.getWorld() instanceof ServerWorld world) {
                    Entity target = world.getEntity(entry.getKey());
                    if (target instanceof LivingEntity livingEntity) {
                        SoundUtils.playSound(player, SoundRegistry.RATIO_IMPACT_2, 20, 21);
                        SoundUtils.playSound(player, SoundRegistry.RATIO_IMPACT_1, 10, 11);
                        // Todo: make this scale instead, based on entity max health
                        livingEntity.damage(target.getDamageSources().playerAttack(player), livingEntity.getMaxHealth() * 0.3f);

                        player.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.INVERT, 3, 0, true, false, false));
                        CameraUtils.playCameraShake(player);
                        EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("resonance_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(0.5, 0.5, 0.5));
                        EffekUtils.playEffect(BoundlessAPI.id("dismantle_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), target.getHeight() / 16);
                    }
                    iterator.remove();
                }
            }

            ComponentUtils.set(player, StrawdollComponents.RESONANCE_TRACKER, resonanceTracker);
        });
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("resonance");
    }

    public float getDamage(PlayerEntity player) {
        return 20;
    }

    public int getCost(PlayerEntity player) {
        return 1000;
    }

    public long getCooldown(PlayerEntity player) {
        return 300;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Resonance");
    }
}