package com.boundless.ability.reusable_abilities.flight;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.item.ItemStack;

public class FlightAbilities {
    public static Ability BOOST = Ability.builder()
            .abilityConsumer((player) -> {
                ItemStack heroStack = HeroUtils.getHeroStack(player);

                if (player.getWorld().getTime() >= heroStack.getOrDefault(FlightAbility.BOOST_NEXT_USABLE, 0L) && !heroStack.getOrDefault(FlightAbility.BOOSTING, false)) {
                    DataComponentUtils.setInt(FlightAbility.BOOST_TICKS, player, 0);
                }

                if (DataComponentUtils.getInt(FlightAbility.BOOST_TICKS, player, 0) >= 20) {
                    heroStack.set(FlightAbility.BOOSTING, false);
                    heroStack.set(FlightAbility.BOOST_NEXT_USABLE, player.getWorld().getTime() + 40);
                } else {
                    heroStack.set(FlightAbility.BOOSTING, true);
                    DataComponentUtils.addOrSubtractInt(FlightAbility.BOOST_TICKS, player, 1, 20);
                }
            })
            .cooldown(0)
            .abilityID(BoundlessAPI.identifier("boost"))
            .build();
}
