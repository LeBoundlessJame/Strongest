package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DontMoveAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        Vec3d effectScale =  new Vec3d(player.getScale() * 0.4f, player.getScale() * 0.4f, player.getScale() * 0.4f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(player.getRotationVector().x, player.getStandingEyeHeight(), player.getRotationVector().z);
        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), player, effectScale, effectRotation, effectPosition);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(player);

        for (LivingEntity target: targets) {
            CombatUtils.slow(target, 80, 255);
        }
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 400;
    }

    @Override
    public int getCost(PlayerEntity player) {
        return 1000;
    }

    @Override
    public Identifier getId() {
        return BoundlessAPI.id("dont_move");
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Say 'Don't move'");
    }
}
