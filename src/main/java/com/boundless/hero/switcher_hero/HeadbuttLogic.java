package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class HeadbuttLogic {

    public static void headbutt(PlayerEntity player) {
        if (!AttackUtils.canAttack(player)) return;
        int attackCount = AttackUtils.incrementedAttackCount(player);

        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        BiConsumer<PlayerEntity, HeroActionEntity> hook = (user, heroAction) -> {
            if (CombatUtils.isRolling(player)) return;
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, BlackSparksHero.DAMAGE.lightAttack.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        };
        tasks.put(4, hook);
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("grab_and_punch"), 1.0f, attackCount % 2 == 0, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        AttackUtils.startAttackTimer(player, 4);
    }
}
