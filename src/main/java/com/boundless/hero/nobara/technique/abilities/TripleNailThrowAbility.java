package com.boundless.hero.nobara.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.tick.TickScheduler;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TripleNailThrowAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        List<Float> rotations = List.of(-45f, 0f, 45f);

        TickScheduler.schedule(player.getWorld(), 1, () -> {
            for (float rotation: rotations) {
                Vec3d direction = player.getRotationVector().rotateY((float) Math.toRadians(-rotation / 6));
                NailEntity nailEntity = new NailEntity(player, player.getWorld());
                nailEntity.setVelocity(direction.multiply(4));
                nailEntity.setPosition(player.getPos().add(direction.multiply(2).x, 1, direction.multiply(2).z));
                nailEntity.setNoGravity(true);
                nailEntity.setPitch(player.getPitch());
                nailEntity.setYaw(player.getYaw() + rotation);
                EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("curse_fire"), nailEntity, new Vec3d(0.5, 0.5, 0.5), new Vec3d(player.getPitch(), player.getYaw() * -1, 0));
                player.getWorld().spawnEntity(nailEntity);
            }
        });
    }

    public long getCooldown(PlayerEntity player) {
        return 600;
    }

    public int getCost(PlayerEntity player) {
        return 2000;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Triple nail throw");
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("triple_nail_throw");
    }
}
