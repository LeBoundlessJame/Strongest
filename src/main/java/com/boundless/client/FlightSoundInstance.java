package com.boundless.client;

import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.util.HeroUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class FlightSoundInstance extends MovingSoundInstance {
    private final ClientPlayerEntity player;
    private int tickCount = 0;

    public FlightSoundInstance(ClientPlayerEntity player) {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void tick() {
        ++this.tickCount;

        if (!this.player.isRemoved() && (this.tickCount <= 20 || HeroUtils.getHeroStack(this.player).getOrDefault(FlightAbility.FLIGHT_TICKS, 0) > 0)) {
            this.x = this.player.getX();
            this.y = this.player.getY();
            this.z = this.player.getZ();

            this.volume = 1.0f;

            this.pitch = 1.0F + (this.volume - 0.8F);
        } else {
            this.setDone();
        }
    }
}
