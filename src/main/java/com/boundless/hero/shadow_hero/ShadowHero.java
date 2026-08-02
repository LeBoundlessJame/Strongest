package com.boundless.hero.shadow_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.BasicAbilities;
import com.boundless.ability.HeldAbility;
import com.boundless.action.Action;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.black_sparks_hero.*;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.ConfigRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ShadowHero extends Hero {
    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public static Ability SUMMON_KURO = AbilityUtils.ability((player) -> {
        if (player.getWorld().isClient) return;
        BlockHitResult blockHitResult = RaycastUtils.blockRaycast(player, 16);
        if (blockHitResult == null) return;

        BlockPos pos = blockHitResult.getBlockPos();

        for (int i = 0; i < 64; i++) {
            if (player.getWorld().getBlockState(pos.up(i)) == Blocks.AIR.getDefaultState()) {
                pos = pos.up();
                break;
            }
        }

        DivineDogKuroEntity kuroEntity = new DivineDogKuroEntity(player.getWorld(), player);
        kuroEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
        kuroEntity.setOwner(player);
        player.getWorld().spawnEntity(kuroEntity);

        EffekUtils.playBoundEffect(BoundlessAPI.identifier("divine_dog_summon"), kuroEntity, new Vec3d(0.2f, 0.2f, 0.2f), Vec3d.ZERO);
    }, 1, BoundlessAPI.identifier("summon_kuro"), "Summon Kuro");

    public ShadowHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", ShadowHero.SUMMON_KURO)
                .ability("key.boundless.combat_mode_toggle", BasicAbilities.COMBAT_MODE_TOGGLE)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("shadow_hero")
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("brawler"))
                .textureIdentifier(BoundlessAPI.textureID("brawler"))
                .tickHandler(Hero::onHeroTick)
                .build();
        this.registerHero();
    }
}
