package com.boundless.hero.api;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.hero.armor.HeroArmorRenderer;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import mod.azure.azurelib.common.render.armor.AzArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Builder
@Getter
public class HeroData {
    private final String name;
    @Builder.Default
    private final String displayName = "hero";

    @Singular
    private final List<Consumer<PlayerEntity>> tickHandlers;

    @Singular
    private final List<Consumer<MinecraftClient>> clientTickEvents;

    private AbilityLoadout defaultAbilityLoadout;

    @Builder.Default
    private final AttributeModifiersComponent attributes = AttributeModifiersComponent.builder().build();

    @Builder.Default
    private final Identifier textureIdentifier = BoundlessAPI.textureID("hero");
    @Builder.Default
    private final Identifier modelIdentifier = BoundlessAPI.modelID("hero");

    private final BiConsumer<DrawContext, RenderTickCounter> hudRenderer;

    @Builder.Default
    private final BiFunction<Identifier, Identifier, ? extends AzArmorRenderer> armorRenderer = HeroArmorRenderer::new;

    @Builder.Default
    private final BiConsumer<PlayerEntity, HeroActionEntity> customReviveLogic = null;

    @Singular
    private final List<String> heldKeybinds;

    private final Function<ItemStack, List<Text>> customTooltips;
}
