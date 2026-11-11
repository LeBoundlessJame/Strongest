package com.boundless.hero.nobara.technique.abilities;


import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class ResonanceAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        ComponentUtils.incrementInt(DataComponentRegistry.ATTACK_COUNT, player, 1);
        int attackCount = HeroUtils.getHeroStack(player).getOrDefault(DataComponentRegistry.ATTACK_COUNT, 0);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("dismantle_1"), 1.5f, attackCount % 2 == 0, true, 3000);

        EntityHitResult result = RaycastUtils.raycast(player, 64);
        Entity target = result == null ? RaycastUtils.thickRaycast(player, 64, 1.5f) : result.getEntity();

        if (target == null || target == player) return;
        target.damage(target.getDamageSources().playerAttack(player), this.getDamage(player));

        //SoundUtils.playSound(player, SoundRegistry.RATIO_IMPACT_2, 20, 21);

        SoundUtils.playSound(player, SoundRegistry.RATIO_IMPACT_1, 10, 11);
        EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("resonance_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), new Vec3d(1, 1, 1));
        EffekUtils.playEffect(BoundlessAPI.id("dismantle_impact"), target, target.getPos().add(0, target.getHeight() / 2, 0), target.getHeight() / 16);
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("hairpin");
    }

    public float getDamage(PlayerEntity player) {
        return 20;
    }

    public int getCost(PlayerEntity player) {
        return 200;
    }

    public long getCooldown(PlayerEntity player) {
        return 10;
    }

    public Text getDisplayText(PlayerEntity playerEntity) {
        return Text.of("Hairpin");
    }
}