package com.boundless.hero.api;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
}
