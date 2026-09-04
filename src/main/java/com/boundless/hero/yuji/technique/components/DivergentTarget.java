package com.boundless.hero.yuji.technique.components;

import com.boundless.hero.nanami.technique.RatioSkillcheck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record DivergentTarget(UUID uuid, float damage) {
    public static final Codec<DivergentTarget> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(Uuids.CODEC.fieldOf("uuid")
                .forGetter(DivergentTarget::uuid), Codec.FLOAT.fieldOf("damage")
                .forGetter(DivergentTarget::damage)).apply(builder, DivergentTarget::new);
    });
}
