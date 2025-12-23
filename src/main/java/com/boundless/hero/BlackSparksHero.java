package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.AbilityUtils;
import com.boundless.util.CameraUtils;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;

public class BlackSparksHero extends Hero {
    public static ComponentType<Long> BLACK_FLASH_TIMESTAMP = DataComponentRegistry.registerComponent("black_flash_time",builder -> ComponentType.<Long>builder().codec(Codec.LONG));

    public static Ability BLACK_FLASH = AbilityUtils.ability(BlackSparksHero::blackFlash, 5, BoundlessAPI.identifier("black_flash"), BoundlessAPI.identifier("arm"));

    public BlackSparksHero() {
        AbilityLoadout loadout = AbilityLoadout.builder()
                .ability("key.attack", BlackSparksHero.BLACK_FLASH)
                .ability("key.boundless.ability_one", MeleeCombatAbilities.DODGE)
                .ability("key.boundless.ability_two", MeleeCombatAbilities.SPIN_KICK)
                .build();

        ABILITY_LOADOUTS.put("LOADOUT_1", loadout);
        this.heroData = HeroData.builder()
                .name("black_sparks_hero")
                .textureIdentifier(BoundlessAPI.textureID("black_sparks_hero"))
                .defaultAbilityLoadout(loadout)
                .build();
        this.registerHero();
    }

    public static void blackFlash(PlayerEntity player) {
        AttackDataBuilder data = AttackDataBuilder
                .builder()
                .damage(800)
                .knockbackStrength(2)
                .impactSound(SoundRegistry.BLACK_FLASH)
                .impactTick(4)
                .animation(BoundlessAPI.identifier("hook"))
                .impactVisual(BoundlessAPI.identifier("black_flash_impact"))
                .postHitLogic(CameraUtils::playCameraShake)
                .attacker(player)
                .build();
        MeleeAbility blackSparks = new MeleeAbility(data);
        blackSparks.attack(player);
    }


    /*
    public static Ability BLACK_FLASH = Ability.builder()
            .abilityLogic((player) -> {
                new MeleeAbility(AttackDataBuilder.builder()
                        .damage(40f)
                        .knockbackStrength(2f)
                        .animation(BoundlessAPI.identifier("hook"))
                        .impactTick(4)
                        .customHitLogic((attackDataBuilder, livingEntity) -> {
                            SoundUtils.playSound(player, SoundRegistry.BLACK_FLASH, 1.0f);
                            if (!player.getWorld().isClient) {
                                ServerPlayNetworking.send((ServerPlayerEntity) player, new CameraShakePayload());
                            }
                            CombatUtils.uppercutLogic(attackDataBuilder, livingEntity);

                            //EffekUtils.playRotatedEffect(BoundlessAPI.identifier("black_flash_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), new Vec3d(1, 1, 1), new Vec3d(0, 0, 0));
                        })
                        .player(player)
                        .build()).attack(player);
            })
            .cooldown(5)
            .abilityID(BoundlessAPI.identifier("black_flash"))
            .abilityIcon(BoundlessAPI.hudPNG("arm"))
            .build();

     */
}
