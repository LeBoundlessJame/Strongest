package com.boundless.loadouts;

import lombok.Getter;

public enum AbilityKey {
    ATTACK("key.attack"),
    USE("key.use"),
    ABILITY_ONE("key.boundless.ability_one"),
    ABILITY_TWO("key.boundless.ability_two"),
    ABILITY_THREE("key.boundless.ability_three"),
    ABILITY_FOUR("key.boundless.ability_four");

    AbilityKey(String translationKey) {
        this.translationKey = translationKey;
    }

    @Getter
    private final String translationKey;
}
