package com.boundless.hero.nobara.technique.abilities;


import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.hero.nobara.technique.components.CursedNailData;
import com.boundless.hero.nobara.technique.components.StrawdollComponents;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class HairpinAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, user, 1);
        int attackCount = HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);

        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);
        SoundUtils.playSound(user, SoundRegistry.CLICK, 10, 11);

        detonateImpaledNails(user);
        detonateGroundNails(user);
    }

    // Nails that hit the enemy successfully
    public void detonateImpaledNails(LivingEntity user) {
        Map<UUID, List<CursedNailData>> targets = ComponentUtils.getOr(user, StrawdollComponents.HAIRPIN_TARGETS, new HashMap<>());
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

                if (!nail.isExpired(user.getWorld().getTime())) {
                    damage += 7.5f;
                } else {
                    nailIterator.remove();
                }
            }

            if (damage > 0 && user.getWorld() instanceof ServerWorld world) {
                Entity entity = world.getEntity(entry.getKey());
                if (entity instanceof LivingEntity target) {
                    target.damage(target.getDamageSources().mobAttack(user), damage);
                    EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("hairpin_entity_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(0.5, 0.5, 0.5));
                    EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("dismantle_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(0.35, 0.35, 0.35));
                }
            }

            iterator.remove();
        }

        ComponentUtils.set(user, StrawdollComponents.HAIRPIN_TARGETS, targets);
    }

    public void detonateGroundNails(LivingEntity user) {
        List<Entity> nails = AOEUtils.getEntitiesInRadius(user, user.getWorld(), user.getPos(), new Vec3d(64, 32, 64), entity -> entity instanceof NailEntity);

        for (Entity nail : nails) {
            if (nail instanceof NailEntity nailEntity) {
                if (nailEntity.getOwner() == user) {
                    nailEntity.detonate();
                }
            }
        }
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("hairpin");
    }

    public int getCost(LivingEntity user) {
        return 200;
    }

    public long getCooldown(LivingEntity user) {
        return 20;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Hairpin");
    }
}