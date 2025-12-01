package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class BlackSparksHero extends Hero {
    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.BLACK_FLASH)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .build();
        this.registerHero();
    }

    public static Ability BLACK_FLASH = Ability.builder()
            .abilityConsumer((player) -> {
                EffekUtils.playRotatedEffect(BoundlessAPI.identifier("black_flash_impact"), player, player.getPos(), new Vec3d(1, 1, 1), new Vec3d(0, 0, 0));
                System.out.println("Koukousen!");
                SoundUtils.playSound(player, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, 1.8f);
            })
            .cooldown(5)
            .abilityID(BoundlessAPI.identifier("black_flash"))
            .abilityIcon(BoundlessAPI.hudPNG("arm"))
            .build();
}
