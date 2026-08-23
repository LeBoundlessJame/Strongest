package com.boundless.hero.shadow_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.BasicAbilities;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.entity.divine_dogs.shiro.DivineDogShiroEntity;
import com.boundless.entity.gama.GamaEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.black_sparks_hero.BrawlerHUD;
import com.boundless.registry.AttributeRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.ShikigamiUtils;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

import static com.boundless.registry.DataComponentRegistry.registerComponent;

public class ShadowHero extends Hero {
    public static final ComponentType<Boolean> GRAPPLING = registerComponent("grappling", builder -> ComponentType.<Boolean>builder().codec(Codec.BOOL));
    public static final ComponentType<Integer> BOUND_GRAPPLE_HOOK_ID = registerComponent("bound_grapple_hook_id", builder -> ComponentType.<Integer>builder().codec(Codec.INT));
    public static final ComponentType<Map<String, NbtCompound>> SHIKIGAMI = registerComponent("shikigami", builder -> ComponentType.<Map<String, NbtCompound>>builder().codec(Codec.unboundedMap(Codec.STRING, NbtCompound.CODEC)));

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public static Ability SUMMON_KURO = AbilityUtils.ability((player) -> {
        ShikigamiUtils.toggleShikigami(player, new DivineDogKuroEntity(player.getWorld(), player));
        //EffekUtils.playBoundEffect(BoundlessAPI.identifier("divine_dog_summon"), kuroEntity, new Vec3d(0.2f, 0.2f, 0.2f), Vec3d.ZERO);
    }, 1, BoundlessAPI.identifier("summon_kuro"), "Summon Kuro");

    public static Ability SUMMON_SHIRO = AbilityUtils.ability((player) -> {
        ShikigamiUtils.toggleShikigami(player, new DivineDogShiroEntity(player.getWorld(), player));
        //EffekUtils.playBoundEffect(BoundlessAPI.identifier("divine_dog_summon"), kuroEntity, new Vec3d(0.2f, 0.2f, 0.2f), Vec3d.ZERO);
    }, 1, BoundlessAPI.identifier("summon_shiro"), "Summon Shiro");

    public static Ability SUMMON_GAMA = AbilityUtils.ability((player) -> {
        ShikigamiUtils.toggleShikigami(player, new GamaEntity(player.getWorld(), player));
    }, 1, BoundlessAPI.identifier("summon_gama"), "Summon Gama");


    public ShadowHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.boundless.ability_one", ShadowHero.SUMMON_KURO)
                .ability("key.boundless.ability_two", ShadowHero.SUMMON_SHIRO)
                .ability("key.boundless.ability_three", ShadowHero.SUMMON_GAMA)
                .ability("key.boundless.combat_mode_toggle", BasicAbilities.COMBAT_MODE_TOGGLE)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("shadow_hero")
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .modelIdentifier(BoundlessAPI.modelID("shadow_hero"))
                .textureIdentifier(BoundlessAPI.textureID("shadow_hero"))
                .tickHandler(Hero::onHeroTick)
                .build();
        this.registerHero();
    }
}
