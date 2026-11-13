package com.boundless.hero.nobara.technique.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record ResonanceTracker(UUID targetUUID, float damageDealt, long lastDamagedTimestamp) {
    private static int MAX_TIME_BETWEEN_HITS = 1200;

    public static final Codec<ResonanceTracker> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Uuids.CODEC.fieldOf("target_uuid").forGetter(ResonanceTracker::targetUUID),
                Codec.FLOAT.fieldOf("damage_dealt").forGetter(ResonanceTracker::damageDealt),
                Codec.LONG.fieldOf("last_damaged_timestamp").forGetter(ResonanceTracker::lastDamagedTimestamp))
                .apply(builder, ResonanceTracker::new);
    });

    public boolean isExpired(long currentTick) {
        return currentTick - lastDamagedTimestamp > MAX_TIME_BETWEEN_HITS;
    }
}