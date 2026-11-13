package com.boundless.hero.nobara.technique.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record ResonanceData(float damageDealt, long lastDamagedTimestamp) {
    private static int MAX_TIME_BETWEEN_HITS = 1200;

    public static final Codec<ResonanceData> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.FLOAT.fieldOf("damage_dealt").forGetter(ResonanceData::damageDealt),
                Codec.LONG.fieldOf("last_damaged_timestamp").forGetter(ResonanceData::lastDamagedTimestamp))
                .apply(builder, ResonanceData::new);
    });

    public boolean isExpired(long currentTick) {
        return currentTick - lastDamagedTimestamp > MAX_TIME_BETWEEN_HITS;
    }
}