package com.boundless.hero.nobara.technique.abilities;


import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.entity.nail.NailEntity;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class HairpinAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        List<Entity> nails = AOEUtils.getEntitiesInRadius(player, player.getWorld(), player.getPos(), new Vec3d(64, 32, 64), entity -> entity instanceof NailEntity);

        for (Entity nail: nails) {
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
        return 40;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Hairpin");
    }
}