package com.boundless.loadouts;

import com.mojang.serialization.Codec;
import lombok.Getter;
import net.minecraft.util.Rarity;
import net.minecraft.util.StringIdentifiable;

public enum AbilityKey implements StringIdentifiable {
    ATTACK("key.attack"),
    USE("key.use"),
    ABILITY_ONE("key.boundless.ability_one"),
    ABILITY_TWO("key.boundless.ability_two"),
    ABILITY_THREE("key.boundless.ability_three"),
    ABILITY_FOUR("key.boundless.ability_four");

    public static final Codec<AbilityKey> CODEC = StringIdentifiable.createBasicCodec(AbilityKey::values);

    AbilityKey(String translationKey) {
        this.translationKey = translationKey;
    }

    @Getter
    private final String translationKey;

    @Override
    public String asString() {
        return this.translationKey;
    }
}
