package com.boundless.hero.inumaki.technique;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.combat.MeleeHitbox;
import com.boundless.mechanics.CursedEnergyManager;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.tick.TickScheduler;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DontMoveAbility extends CursedSpeechAbility {
    @Override
    public void cursedSpeechEffect(PlayerEntity player, LivingEntity target) {
        CombatUtils.slow(target, 80, 255);
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
