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
import com.boundless.hero.MeleeHero;
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
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class BlackSparksHero extends Hero {
    public static long CE_TIME_WINDOW = 60;
    public static BlackSparksHeroConfig.AbilityDamageConfig DAMAGE = ConfigRegistry.HERO_CONFIG.BLACK_SPARKS_CONFIG.abilityDamageConfig;

    public static Ability LIGHT_ATTACK = AbilityUtils.ability(BlackSparksHero::lightAttack, 5, BoundlessAPI.identifier("yuji_light"), BoundlessAPI.hudPNG("arm"));
    public static Ability MEDIUM_ATTACK = AbilityUtils.ability(BlackSparksHero::mediumAttack, 5, BoundlessAPI.identifier("yuji_medium"), BoundlessAPI.hudPNG("leg"));
    public static Ability SPIN_KICK = AbilityUtils.ability(BlackSparksHero::spinKick, 20, BoundlessAPI.identifier("spin_kick"), BoundlessAPI.hudPNG("spin_kick"));
    public static Ability CHANNEL_CURSED_ENERGY = AbilityUtils.ability(BlackSparksHero::channelCursedEnergy, 2, BoundlessAPI.identifier("channel_cursed_energy"), BoundlessAPI.hudPNG("channel_cursed_energy"));
    public static ComponentType<Long> CHANNEL_CURSED_ENERGY_TIMESTAMP = DataComponentRegistry.registerComponent("black_flash_time", builder -> ComponentType.<Long>builder().codec(Codec.LONG));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.forEquipmentSlot(EquipmentSlot.CHEST))
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.80f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.forEquipmentSlot(EquipmentSlot.CHEST))
            .build();

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.LIGHT_ATTACK)
                .ability("key.use", BlackSparksHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_two", BlackSparksHero.SPIN_KICK)
                .ability("key.boundless.ability_three", BlackSparksHero.CHANNEL_CURSED_ENERGY)
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

    public static void lightAttack(PlayerEntity player) {
        DataComponentUtils.incrementInt(MeleeHero.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(MeleeHero.ATTACK_COUNT, player, 0);

        if (channelCursedEnergyActive(player)) {
            blackFlash(player);
            HeroUtils.getHeroStack(player).set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
        } else {
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            BiConsumer<PlayerEntity, HeroActionEntity> hook = (user, heroAction) -> {
                SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
                CombatUtils.attack(heroAction, DAMAGE.lightAttack.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
            };
            tasks.put(4, hook);
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("hook"), 1.0f, attackCount % 2 == 0, true, 2000);
            ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        }
    }

    public static void mediumAttack(PlayerEntity player) {
        DataComponentUtils.incrementInt(MeleeHero.ATTACK_COUNT, player, 1);
        int attackCount = DataComponentUtils.getInt(MeleeHero.ATTACK_COUNT, player, 0);

        if (channelCursedEnergyActive(player)) {
            divergentFist(player);
            HeroUtils.getHeroStack(player).set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
        } else {
            LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> tasks = new LinkedHashMap<>();
            BiConsumer<PlayerEntity, HeroActionEntity> kick = (user, heroAction) -> {
                SoundUtils.playSound(player, SoundRegistry.EARTH_IMPACT);
                CombatUtils.attack(heroAction, DAMAGE.spinKick.get(), Optional.of(BoundlessAPI.identifier("melee_impact")));
                CombatUtils.perEnemyLogic(heroAction, (attacker, target) -> {
                    target.addVelocity(0, 0.5f, 0);
                    target.velocityModified = true;
                });
            };
            tasks.put(4, kick);
            tasks.put(8, kick);
            AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("double_kick"), 1.0f, attackCount % 2 == 0, true, 2000);
            ActionUtils.performAction(player, Action.builder().scheduledTasks(tasks).build());
        }
    }

    public static void channelCursedEnergy(PlayerEntity player) {
        ItemStack stack = HeroUtils.getHeroStack(player);
        stack.set(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, channelCursedEnergyActive(player)
                ? player.getWorld().getTime()
                : player.getWorld().getTime() + CE_TIME_WINDOW);
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

    public static boolean channelCursedEnergyActive(PlayerEntity player) {
        return player.getWorld().getTime() < HeroUtils.getHeroStack(player).getOrDefault(BlackSparksHero.CHANNEL_CURSED_ENERGY_TIMESTAMP, player.getWorld().getTime());
    }
}
