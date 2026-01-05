package com.boundless.ability;

import com.boundless.ability.components.KeybindHoldData;
import com.boundless.hero.black_sparks_hero.BlackSparksHero;
import com.boundless.util.KeybindingUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class HeldAbility {
    public String keybind = "key.boundless.ability_one";
    public int cooldown = 20;
    public static int requiredHoldTime = 20;
    public static Consumer<PlayerEntity> abilityLogic = BlackSparksHero::dash;

    public static void holdTickLogic(PlayerEntity player) {
        if (player.getWorld().isClient) return;

        KeybindHoldData data = KeybindingUtils.getHoldData(player, "key.boundless.ability_one");
        if (data.startTimestamp() == 0) return;

        if (!data.held()) {
            long heldFor = player.getWorld().getTime() - data.startTimestamp();
            KeybindingUtils.endKeybindHold(player, "key.boundless.ability_one");

            if (heldFor >= requiredHoldTime) {
                abilityLogic.accept(player);
            }
        }
    }
}
