package com.boundless.hero.nobara.technique.abilities;


import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.hero.nobara.technique.StrawdollTechnique;
import com.boundless.hero.nobara.technique.components.CursedNailData;
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

import java.util.*;

public class HairpinAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);
        SoundUtils.playSound(player, SoundRegistry.CLICK);

        detonateImpaledNails(player);
        detonateGroundNails(player);
    }

    // Nails that hit the enemy successfully
    public void detonateImpaledNails(PlayerEntity player) {
        Map<UUID, List<CursedNailData>> targets = ComponentUtils.getOr(player, StrawdollComponents.HAIRPIN_TARGETS, new HashMap<>());
        targets = new HashMap<>(targets);

        // Todo: I don't like this, but it works, will rework soon
        targets.replaceAll((uuid, nails) -> new ArrayList<>(nails));

        Iterator<Map.Entry<UUID, List<CursedNailData>>> iterator = targets.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, List<CursedNailData>> entry = iterator.next();

            Iterator<CursedNailData> nailIterator = entry.getValue().iterator();
            float damage = 0;

            while (nailIterator.hasNext()) {
                CursedNailData nail = nailIterator.next();

                if (!nail.isExpired(player.getWorld().getTime())) {
                    damage += 7.5f;
                } else {
                    nailIterator.remove();
                }
            }

            if (damage > 0 && player.getWorld() instanceof ServerWorld world) {
                Entity entity = world.getEntity(entry.getKey());
                if (entity instanceof LivingEntity target) {
                    target.damage(target.getDamageSources().playerAttack(player), damage);
                    EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("hairpin_entity_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(0.5, 0.5, 0.5));
                }
            }

            iterator.remove();
        }

        ComponentUtils.set(player, StrawdollComponents.HAIRPIN_TARGETS, targets);
    }

    public void detonateGroundNails(PlayerEntity player) {
        List<Entity> nails = AOEUtils.getEntitiesInRadius(player, player.getWorld(), player.getPos(), new Vec3d(64, 32, 64), entity -> entity instanceof NailEntity);

        for (Entity nail : nails) {
            if (nail instanceof NailEntity nailEntity) {
                if (nailEntity.getOwner() == player) {
                    nailEntity.detonate();
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("hairpin");
    }

    public int getCost(PlayerEntity player) {
        return 200;
    }

    public long getCooldown(PlayerEntity player) {
        return 20;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Hairpin");
    }
}