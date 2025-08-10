package com.boundless.networking.payloads.flight;

import com.boundless.ability.reusable_abilities.flight.FlightAbility;
import com.boundless.networking.PayloadRegistry;
import com.boundless.util.HeroUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateFlightDirectionPayload(String direction) implements CustomPayload {
    public static final CustomPayload.Id<UpdateFlightDirectionPayload> ID = new CustomPayload.Id<>(PayloadRegistry.UPDATE_FLIGHT_DIRECTION);

    public static final PacketCodec<RegistryByteBuf, UpdateFlightDirectionPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, UpdateFlightDirectionPayload::direction,
            UpdateFlightDirectionPayload::new);

    public static void receive(UpdateFlightDirectionPayload payload, ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        if (!HeroUtils.isHero(player)) return;
        ItemStack heroStack = HeroUtils.getHeroStack(player);
        heroStack.set(FlightAbility.FLIGHT_DIRECTION, payload.direction);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
