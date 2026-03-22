package com.boundless.hero.shrine_hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.*;
import com.boundless.combat.Combo;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.hero.armor.HeroArmorRenderer;
import com.boundless.hero.black_sparks_hero.BrawlerHUD;
import com.boundless.registry.*;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.MeterUtils;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.boundless.hero.shrine_hero.ShrineHeroMelee.LIGHT_ATTACK;

public class ShrineHero extends Hero {
    public static List<Combo> COMBOS = List.of(new Combo("lllll", ShrineHeroMelee::knockbackAttack, "Heavy Hit"));

    public static ComponentType<Integer> FINGER_COUNT = DataComponentRegistry.registerInt("finger_count");

    public static ShrineConfig CONFIG = ConfigRegistry.HERO_CONFIG.SHRINE_CONFIG;
    public static ShrineConfig.AbilityDamageConfig DAMAGE = CONFIG.ABILITY_DAMAGE_CONFIG;
    public static ShrineConfig.AbilityCooldownConfig COOLDOWNS = CONFIG.ABILITY_COOLDOWN_CONFIG;
    public static ShrineConfig.DomainConfig DOMAIN = CONFIG.DOMAIN_CONFIG;
    public static ShrineConfig.MeterConfig METER_CONFIG = CONFIG.METER_CONFIG;

    public static AttributeModifiersComponent ATTRIBUTES = AttributeModifiersComponent.builder()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_max_health"), 580, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.identifier("generic_jump_strength"), 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
            .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.identifier("generic_safe_fall_damage_distance"), 65, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.identifier("top_speed_multiplier"), 3.5f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
            .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.identifier("ticks_until_max_speed"), 2, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
            .build();

    public ShrineHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", LIGHT_ATTACK)
                .ability("key.use", MeleeAbilities.BLOCK)
                .ability("key.boundless.ability_one", ShrineHeroSlashes.DISMANTLE)
                .ability("key.boundless.ability_two", ShrineHeroSlashes.CLEAVE)
                .ability("key.boundless.ability_three", ShrineHeroDestruction.OPEN)
                .ability("key.boundless.ability_four", AntiDomainTechniques.SIMPLE_DOMAIN)
                .ability("key.boundless.ability_five", ShrineHeroDestruction.SHRINE)
                .ability("key.boundless.combat_mode_toggle", BasicAbilities.COMBAT_MODE_TOGGLE)
                .ability("key.boundless.evasive", MeleeAbilities.DODGE)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("shrine_hero")
                .modelIdentifier(BoundlessAPI.modelID("brawler"))
                .textureIdentifier(BoundlessAPI.textureID("shrine_hero"))
                .defaultAbilityLoadout(loadout)
                .attributes(ATTRIBUTES)
                .hudRenderer(BrawlerHUD::render)
                .tickHandler(Hero::heroSprintHandler)
                .tickHandler(ShrineHero::regenTick)
                .armorRenderer(HeroArmorRenderer::new)
                .tickHandler(Hero::onHeroTick)
                .tickHandler(BlockLogic::tick)
                .tickHandler(SimpleDomain::simpleDomainTick)
                .customTooltips(ShrineHero::customTooltip)
                .combos(COMBOS)
                .heldKeybind("key.use")
                .build();
        this.registerHero();
    }

    public static List<Text> customTooltip(ItemStack stack) {
        int fingersConsumed = stack.getOrDefault(ShrineHero.FINGER_COUNT, 0);
        MutableText mutableText = Text.literal("Fingers Consumed: " + fingersConsumed).formatted(Formatting.RED, Formatting.BOLD);
        return List.of(mutableText);
    }

    // Todo: Make it so that yuji can also eat the finger
    public static boolean canEatFinger(LivingEntity livingEntity) {
        return livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem().equals(HeroRegistry.SHRINE_HERO.getArmorSet().get(1));
    }

    // Todo: make this figure configurable
    public static void regenTick(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (player.age % 20 != 0) return;

        if (player.getLastAttacker() == null || player.age - player.getLastAttackedTime() > 1200) {
            player.heal(player.getMaxHealth() / 30);
            // Todo: make this not hard coded, it should get based on the player's reserves
            MeterUtils.regenMeter(player, 10000 / 120);
        }
    }
}