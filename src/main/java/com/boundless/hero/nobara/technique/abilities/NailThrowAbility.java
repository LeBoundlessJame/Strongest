package com.boundless.hero.nobara.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.ComponentUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.PlayerAnimationUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class NailThrowAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        TickScheduler.schedule(player.getWorld(), 1, () -> {
            NailEntity nailEntity = new NailEntity(player, player.getWorld());
            nailEntity.setVelocity(player.getRotationVector().multiply(4));
            nailEntity.setPosition(player.getPos().add(player.getRotationVector().multiply(2).x, 1, player.getRotationVector().multiply(2).z));
            nailEntity.setNoGravity(true);
            nailEntity.setPitch(player.getPitch());
            nailEntity.setYaw(player.getYaw());
            EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("curse_fire"), nailEntity, new Vec3d(0.5, 0.5, 0.5), new Vec3d(player.getPitch(), player.getYaw() * -1, 0));
            player.getWorld().spawnEntity(nailEntity);
        });
    }

    public long getCooldown(PlayerEntity player) {
        return 20;
    }

    public int getCost(PlayerEntity player) {
        return 2000;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Nail throw");
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("nail_throw");
    }
}
