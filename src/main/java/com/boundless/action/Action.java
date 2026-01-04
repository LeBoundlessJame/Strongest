package com.boundless.action;

import com.boundless.entity.hero_action.HeroActionEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

@Builder
@Getter
@Setter
public class Action {
    public LinkedHashMap<Integer, BiConsumer<PlayerEntity, HeroActionEntity>> scheduledTasks;
    public BiConsumer<PlayerEntity, HeroActionEntity> customTickLogic;
    @Builder.Default
    public String renderLogicID = "fr";

    @Builder.Default
    float hitboxWidthX = 4;
    @Builder.Default
    float hitboxHeight = 2;
    @Builder.Default
    float hitboxWidthZ = 2;
}
