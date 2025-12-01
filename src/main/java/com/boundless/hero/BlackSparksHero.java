package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.ability.AbilityLoadout;
import com.boundless.ability.MeleeAbility;
import com.boundless.ability.combat.AttackDataBuilder;
import com.boundless.ability.reusable_abilities.MeleeCombatAbilities;
import com.boundless.hero.api.Hero;
import com.boundless.hero.api.HeroData;
import com.boundless.networking.payloads.CameraShakePayload;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import com.boundless.util.SoundUtils;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.ComponentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class BlackSparksHero extends Hero {
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


    /*
    Vec3d effectScale =  new Vec3d(livingEntity.getScale() * 0.5f, livingEntity.getScale() * 0.5f, livingEntity.getScale() * 0.5f);
    Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
    EffekUtils.playRotatedEffect(BoundlessAPI.identifier("divergent_fist_impact"), player, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), effectScale, effectRotation);
     */
    public static Ability BLACK_FLASH =  Ability.builder()
            .abilityConsumer((player) -> {
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
                            Vec3d effectScale =  new Vec3d(livingEntity.getScale() * 0.5f, livingEntity.getScale() * 0.5f, livingEntity.getScale() * 0.5f);
                            Vec3d effectRotation = new Vec3d(player.getPitch(), player.getYaw() * -1, 0);
                            EffekUtils.playRotatedEffect(BoundlessAPI.identifier("black_flash_impact"), player, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), effectScale, effectRotation);

                            //EffekUtils.playRotatedEffect(BoundlessAPI.identifier("black_flash_impact"), livingEntity, livingEntity.getPos().add(0, livingEntity.getHeight() / 2, 0), new Vec3d(1, 1, 1), new Vec3d(0, 0, 0));
                        })
                        .player(player)
                        .build()).singleAttack(player);
            })
            .cooldown(5)
            .abilityID(BoundlessAPI.identifier("black_flash"))
            .abilityIcon(BoundlessAPI.hudPNG("arm"))
            .build();
}
