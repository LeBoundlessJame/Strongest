package com.boundless.hero.black_sparks_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.action.Action;
import com.boundless.config.HeroConfig;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.*;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;

    public static ComponentType<Long> BLACK_FLASH_TIMESTAMP = DataComponentRegistry.registerComponent("black_flash_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));
    public static Ability BLACK_FLASH = AbilityUtils.ability(BlackSparksHero::blackFlash, 5, BoundlessAPI.identifier("black_flash"), BoundlessAPI.hudPNG("black_flash"));
    public static Ability DIVERGENT_FIST = AbilityUtils.ability(BlackSparksHero::divergentFist, 5, BoundlessAPI.identifier("divergent_fist"), BoundlessAPI.hudPNG("divergent_fist"));
    public static Ability SPIN_KICK = AbilityUtils.ability(BlackSparksHero::spinKick, 20, BoundlessAPI.identifier("spin_kick"), BoundlessAPI.hudPNG("leg"));
    public static Ability DOUBLE_KICK = AbilityUtils.ability(BlackSparksHero::doubleKick, 10, BoundlessAPI.identifier("double_kick"), BoundlessAPI.hudPNG("leg"));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(EquipmentSlot.CHEST))
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.80f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.forEquipmentSlot(EquipmentSlot.CHEST))
            .build();

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.BLACK_FLASH)
                .ability("key.use", BlackSparksHero.DOUBLE_KICK)
                .ability("key.boundless.ability_one", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_two", BlackSparksHero.SPIN_KICK)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BlackSparksHUD::render)
                .build();
        this.registerHero();
    }

    public static void blackFlash(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(DAMAGE.blackFlash.get())
                .knockbackStrength(2)
                .impactSound(SoundRegistry.BLACK_FLASH)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("black_flash_impact"))
                .attacker(player)
                .build();
        MeleeAbility blackSparks = new MeleeAbility(data);
        blackSparks.attack(player);
        CameraUtils.playCameraShake(player);
        if (player.getWorld().isClient) return;
        player.sendMessage(Text.of("§c§l§ka§c §c§lKOKUSEN! §c§l§ka§c"), true);
    }

    public static void divergentFist(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistPunch.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        tasks.put(8, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.BLACK_FLASH);
            CameraUtils.playCameraShake(player);
            CombatUtils.attack(heroAction, DAMAGE.divergentFistImpact.get(), Optional.of(BoundlessAPI.identifier("divergent_fist_impact")));
        });
        Action divergence = Action.builder().scheduledTasks(tasks).build();

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"));
        ActionUtils.performAction(player, divergence);
    }

    public static void spinKick(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(7, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.knockbackAttack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("spin_kick"), 1.0f, false, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        player.addVelocity(player.getRotationVector().normalize().multiply(0.4f).x, player.isOnGround() ? 0.5f : 0.0f, player.getRotationVector().normalize().multiply(0.4f).z);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 7, 2, true, false, false));
        player.velocityModified = true;
    }

    public static void doubleKick(PlayerEntity player) {
        LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
        tasks.put(4, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
            CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                target.addVelocity(0, 0.5f, 0);
                target.velocityModified = true;
            });
        });
        tasks.put(8, (user, heroAction) -> {
            SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
            CombatUtils.attack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
            CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                target.addVelocity(0, 0.5f, 0);
                target.velocityModified = true;
            });
        });
        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("double_kick"), 1.0f, false, true, 2000);
        ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
    }


}
