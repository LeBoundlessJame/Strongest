package com.boundless.mixin.flight_ability;

import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.client.FlightSoundInstance;
import com.boundless.util.HeroUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.ElytraSoundInstance;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FlightSoundsMixin {
    @Shadow @Final protected MinecraftClient client;
    @Unique long nextSoundUpdate = 0L;

    // Todo: Relegate this to a client tick consumer down the line so that it only triggers if flight ability is there
    @Inject(method = "tick", at = @At("TAIL"))
    public void boundless$tick(CallbackInfo ci) {
        if (client == null) return;
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        if (HeroUtils.getHeroStack(player).getOrDefault(FlightAbility.FLIGHT_TICKS, 0) > 0 && player.getWorld().getTime() > nextSoundUpdate) {
            client.getSoundManager().play(new FlightSoundInstance(player));
            nextSoundUpdate = player.getWorld().getTime() + 20;
        }
    }
}
