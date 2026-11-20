package com.boundless.hero.nobara.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.ComponentUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.AnimationManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class NailThrowAbility extends TechniqueAbility {
    @Override
    public void activate(LivingEntity user) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, user, 1);
        int attackCount = HeroUtils.getHeroStack(user).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        TickScheduler.schedule(user.getWorld(), 1, () -> {
            NailEntity nailEntity = new NailEntity(user, user.getWorld());
            nailEntity.setVelocity(user.getRotationVector().multiply(4));
            nailEntity.setPosition(user.getPos().add(user.getRotationVector().multiply(2).x, 1, user.getRotationVector().multiply(2).z));
            nailEntity.setNoGravity(true);
            nailEntity.setPitch(user.getPitch());
            nailEntity.setYaw(user.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("curse_fire"), nailEntity, new Vec3d(0.5, 0.5, 0.5), new Vec3d(user.getPitch(), user.getYaw() * -1, 0));
            user.getWorld().spawnEntity(nailEntity);
        });
    }

    public long getCooldown(LivingEntity user) {
        return 20;
    }

    public int getCost(LivingEntity user) {
        return 100;
    }

    public Text getDisplayText(LivingEntity userEntity) {
        return Text.of("Nail throw");
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("nail_throw");
    }
}
