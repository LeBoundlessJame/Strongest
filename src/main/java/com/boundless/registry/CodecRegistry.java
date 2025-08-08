package com.boundless.registry;

import com.boundless.ability.components.KeybindHoldData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class CodecRegistry {
    public static final Codec<KeybindHoldData> KEYBIND_HOLD_CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.BOOL.fieldOf("held").forGetter(KeybindHoldData::held),
            Codec.LONG.fieldOf("startTimestamp").forGetter(KeybindHoldData::startTimestamp),
            Codec.LONG.fieldOf("endTimestamp").forGetter(KeybindHoldData::endTimestamp)
    ).apply(builder, KeybindHoldData::new));

    public static void initialize() {}
}
