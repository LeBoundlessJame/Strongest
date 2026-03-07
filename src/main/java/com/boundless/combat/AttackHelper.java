package com.boundless.combat;

import com.boundless.BoundlessAPI;
import com.boundless.ability.MeleeAbilities;
import com.boundless.action.SingleAttack;
import com.boundless.hero.shrine_hero.ShrineHelper;
import com.boundless.hero.shrine_hero.ShrineHero;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AttackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

// I'd normally put helpers in with util but here feels like a nice place for it!
public class AttackHelper {
    public static void meleeAttack(PlayerEntity player, float damage, int duration, int impactTick, Identifier anim, float animSpeed, BiConsumer<PlayerEntity, Entity> perEnemyLogic, SoundEvent sound) {
        SingleAttack attack = SingleAttack.builder()
                .player(player)
                .damage(damage)
                .impactSound(sound)
                .animationSpeed(animSpeed)
                .animation(anim)
                .impactTick(impactTick)
                .attackDuration(duration)
                .perEntityLogic(perEnemyLogic)
                .build();
        AttackUtils.performAttack(attack);
    }
}
