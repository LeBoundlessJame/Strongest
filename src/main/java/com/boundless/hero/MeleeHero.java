package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public class MeleeHero extends Hero {
    public static ComponentType<Integer> ATTACK_COUNT = DataComponentRegistry.registerComponent("attack_count", builder -> ComponentType.<Integer>builder().codec(Codec.INT));

    public MeleeHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", MeleeCombatAbilities.JAB)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("melee_hero")
                .textureIdentifier(BoundlessAPI.textureID("meleehero"))
                .defaultAbilityLoadout(loadout)
                .build();
        this.registerHero();
    }
}
