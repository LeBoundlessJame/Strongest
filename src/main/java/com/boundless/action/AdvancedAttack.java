package com.boundless.action;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.function.BiConsumer;

@Builder @Getter
public class AdvancedAttack {
    PlayerEntity player;
    @Builder.Default
    int attackDuration = 10;
    @Singular
    Map<Integer, BiConsumer<PlayerEntity, Entity>> hits;
}
