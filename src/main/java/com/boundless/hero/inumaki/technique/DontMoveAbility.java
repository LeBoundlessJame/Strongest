package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DontMoveAbility extends TechniqueAbility {
    @Override
    public void activate(PlayerEntity player) {
        Vec3d effectScale =  new Vec3d(player.getScale() * 0.5f, player.getScale() * 0.5f, player.getScale() * 0.5f);
        Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
        Vec3d effectPosition = new Vec3d(0, player.getStandingEyeHeight(), 0); //player.getPos().add(0, player.getHeight(), 0);
        EffekUtils.playBoundRotatedEffect(BoundlessAPI.id("cursed_speech"), player, effectScale, effectRotation, effectPosition);

        List<LivingEntity> targets = new MeleeHitbox(16.0f, 60.0).getTargetsInArc(player);

        for (LivingEntity target: targets) {
            CombatUtils.slow(target, 100, 3);
        }

        //player.sendMessage(Text.of("Don't move."));
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 20;
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
