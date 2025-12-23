package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static ComponentType<Long> BLACK_FLASH_TIMESTAMP = DataComponentRegistry.registerComponent("black_flash_time",builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static Ability BLACK_FLASH = AbilityUtils.ability(BlackSparksHero::blackFlash, 5, BoundlessAPI.identifier("black_flash"), BoundlessAPI.hudPNG("black_flash"));
    public static Ability DIVERGENT_FIST = AbilityUtils.ability(BlackSparksHero::divergentFist, 5, BoundlessAPI.identifier("divergent_fist"), BoundlessAPI.hudPNG("divergent_fist"));

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.BLACK_FLASH)
                .ability("key.use", BlackSparksHero.DIVERGENT_FIST)
                .ability("key.boundless.ability_one", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_two", MeleeCombatAbilities.SPIN_KICK)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .build();
        this.registerHero();
    }

    public static void blackFlash(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(200)
                .knockbackStrength(2)
                .impactSound(SoundRegistry.BLACK_FLASH)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("black_flash_impact"))
                .attacker(player)
                .build();
        MeleeAbility blackSparks = new MeleeAbility(data);
        blackSparks.attack(player);
    }

    /*
    public static void divergentFist(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(15)
                .knockbackStrength(4)
                .impactSound(SoundRegistry.EARTH_IMPACT)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("divergent_fist_impact"))
                .postHitLogic(CameraUtils::playCameraShake)
                .attacker(player)
                .build();
        MeleeAbility divergentFist = new MeleeAbility(data);
        divergentFist.attack(player);
    }

     */

    public static void divergentFist(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            CombatUtils.attack(heroAction, 15f, java.util.Optional.empty());
        });
        tasks.put(8, (user, heroAction) -> {
            CameraUtils.playCameraShake(player);
            CombatUtils.attack(heroAction, 1000f, Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
            SoundUtils.playSound(player, SoundRegistry.IMPACT_HEAVY_1);
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
    }

}
