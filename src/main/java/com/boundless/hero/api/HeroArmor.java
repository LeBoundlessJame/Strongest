package com.boundless.hero.api;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.registry.AttributeRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.StrongestComponents;
import lombok.Getter;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HeroArmor extends ArmorItem {
    @Getter
    public HeroData heroData;

    // Todo: Need to remove AbilityLoadout later after I convert everything over to this new system
    public static Settings getSettings(HeroData heroData) {
        Settings settings = new Settings().maxCount(1);

        // Todo: cleanup some of this method soon, I probably don't need ability loadout anymore

        if (heroData.getDefaultAbilityLoadout() != null) {
            HashMap<String, Identifier> loadout = new HashMap<>();

            Map<String, Ability> abilities = heroData.getDefaultAbilityLoadout().getAbilities();
            for (Map.Entry<String, Ability> abilityEntry : new ArrayList<>(abilities.entrySet())) {
                loadout.put(abilityEntry.getKey(), abilityEntry.getValue().getAbilityID());
            }

            settings.component(DataComponentRegistry.ABILITY_LOADOUT, loadout);
        }

        TechniqueLoadout techniqueLoadout = heroData.getDefaultTechniqueLoadout();

        if (techniqueLoadout != null) {
            settings.component(DataComponentRegistry.TECHNIQUE_LOADOUT, techniqueLoadout.asComponent());
        }

        settings.component(DataComponentRegistry.CURSED_ENERGY_MAX, heroData.getMaxCursedEnergy());
        settings.component(DataComponentRegistry.CURSED_ENERGY, heroData.getMaxCursedEnergy());

        settings.component(StrongestComponents.BLACK_FLASH_CHANCE, heroData.getBlackFlashChance());
        settings.component(StrongestComponents.BLACK_FLASH_DAMAGE_MULTIPLIER, heroData.getBlackFlashDamageMultiplier());

        // Todo: I gotta clean all this up, and pass components through the hero creator
        settings.component(StrongestComponents.MELEE_STRENGTH, heroData.getMeleeStrength());
        return settings;
    }

    // Todo: should probably make the settings parameter actually do something soon
    public HeroArmor(RegistryEntry<ArmorMaterial> material, Type type, Settings settings, HeroData heroData) {
        super(material, type, getSettings(heroData));
        this.heroData = heroData;
    }

    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof HeroArmor) {
            if (slot == 2 && !heroData.getTickHandlers().isEmpty()) {
                for (Consumer<PlayerEntity> tickHandler : heroData.getTickHandlers()) {
                    tickHandler.accept(player);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if (heroData.getCustomTooltips() != null) {
            tooltip.addAll(heroData.getCustomTooltips().apply(stack));
        }
    }

    @Override
    public @NotNull AttributeModifiersComponent getAttributeModifiers() {
        if (heroData.getAttributes() != null) return heroData.getAttributes();

        // I can figure out a more clever system later, but this is good for now
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(BoundlessAPI.id("generic_max_health"), heroData.getBonus_health(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
                .add(EntityAttributes.GENERIC_SCALE, new EntityAttributeModifier(BoundlessAPI.id("generic_scale"), heroData.getSize(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, new EntityAttributeModifier(BoundlessAPI.id("generic_jump_strength"), heroData.getJump_strength(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE), AttributeModifierSlot.CHEST)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, new EntityAttributeModifier(BoundlessAPI.id("generic_safe_fall_damage_distance"), heroData.getSafe_fall_distance(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
                .add(AttributeRegistry.TOP_SPEED_MULTIPLIER, new EntityAttributeModifier(BoundlessAPI.id("top_speed_multiplier"), heroData.getTop_speed_multiplier(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), AttributeModifierSlot.CHEST)
                .add(AttributeRegistry.TIME_UNTIL_MAX_SPEED, new EntityAttributeModifier(BoundlessAPI.id("ticks_until_max_speed"), heroData.getTicks_until_max_speed(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.CHEST)
                .build();
    }
}
