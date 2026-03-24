package com.boundless.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TestAbility extends Ability {
    public TestAbility(Identifier abilityID) {
        super(abilityID);
    }

    @Override
    public void executeAbility(PlayerEntity player) {
        player.sendMessage(Text.of("Test ability"));
    }
}
