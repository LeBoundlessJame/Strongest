package com.boundless.hero.switcher_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.action.Action;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.black_sparks_hero.BlackSparksHUD;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

public class SwitcherHero extends Hero {

    public static Ability BOOGIE = AbilityUtils.ability(SwitcherHero::boogie, 5, BoundlessAPI.identifier("boogie"), BoundlessAPI.hudPNG("clap"));

    public static void boogie(PlayerEntity player) {
        if (player.getWorld().isClient()) return;

        Entity target = RaycastUtils.thickRaycast(player, 64, 1.5f);
        if (target == null || target == player) return;

        AnimationUtils.playSyncedAnimation(player, BoundlessAPI.identifier("clap"));
        SoundUtils.playSound(player, SoundRegistry.CLAP_1, 8, 12);

        Vec3d playerPos = player.getPos();
        Vec3d targetPos = target.getPos();

        target.requestTeleport(playerPos.x, playerPos.y, playerPos.z);
        player.requestTeleport(targetPos.x, targetPos.y, targetPos.z);

        player.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, playerPos);
    }

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 20f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.DAMAGE_RESISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("damage_resistance"), 0.75, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 35, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 2.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SCALE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_scale"), 0.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .build();

    public SwitcherHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", SwitcherHero.BOOGIE)
                .ability("key.use", BlackSparksHero.MEDIUM_ATTACK)
                .ability("key.boundless.ability_one", BlackSparksHero.SPIN_KICK)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("switcher_hero")
                .textureIdentifier(BoundlessAPI.textureID("switcher"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BlackSparksHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .armorRenderer(HeroArmorRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .modelIdentifier(BoundlessAPI.modelID("switcher"))
                .build();
        this.registerHero();
    }
}
