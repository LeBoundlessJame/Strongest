package com.boundless.ability.reusable_abilities.flight;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.hero.SuperHero;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.DataComponentUtils;
import com.boundless.util.HeroUtils;
import net.minecraft.item.ItemStack;

public class FlightAbilities {
    public static Ability BOOST = Ability.builder()
            .abilityConsumer((player) -> {
                ItemStack heroStack = HeroUtils.getHeroStack(player);

                if (player.getWorld().getTime() >= heroStack.getOrDefault(SuperHero.BOOST_NEXT_USABLE, 0L) && !heroStack.getOrDefault(SuperHero.BOOSTING, false)) {
                    DataComponentUtils.setInt(SuperHero.BOOST_TICKS, player, 0);
                }

                if (DataComponentUtils.getInt(SuperHero.BOOST_TICKS, player, 0) >= 20) {
                    heroStack.set(SuperHero.BOOSTING, false);
                    heroStack.set(SuperHero.BOOST_NEXT_USABLE, player.getWorld().getTime() + 40);
                } else {
                    heroStack.set(SuperHero.BOOSTING, true);
                    DataComponentUtils.addOrSubtractInt(SuperHero.BOOST_TICKS, player, 1, 20);
                }
            })
            .cooldown(0)
            .abilityID(BoundlessAPI.identifier("boost"))
            .build();
}
