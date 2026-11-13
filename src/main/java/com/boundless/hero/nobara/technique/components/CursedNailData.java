package com.boundless.hero.nobara.technique.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CursedNailData(long hitTimestamp) {
    private static int MAX_AGE = 1200;

    public static final Codec<CursedNailData> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.LONG.fieldOf("hit_timestamp").forGetter(CursedNailData::hitTimestamp))
                .apply(builder, CursedNailData::new);
    });

    public boolean isExpired(long currentTick) {
        return currentTick - hitTimestamp > MAX_AGE;
    }
}