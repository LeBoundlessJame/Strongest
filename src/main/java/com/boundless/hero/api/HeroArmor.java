package com.boundless.hero.api;

import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.registry.DataComponentRegistry;
import lombok.Getter;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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

        return settings;
    }

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
        return heroData.getAttributes();
    }
}
